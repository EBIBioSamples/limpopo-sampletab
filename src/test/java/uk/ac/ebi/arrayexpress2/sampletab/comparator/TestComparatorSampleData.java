package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;

import junit.framework.TestCase;

public class TestComparatorSampleData extends TestCase {

    private SampleData a1;
    private SampleData a2;
    private SampleData b;
    private ComparatorSampleData comp = new ComparatorSampleData();
    
    public void setUp() {
		a1 = new SampleData();
		a1.msi.submissionIdentifier = "A";
        try {
            a1.scd.addNode(new SampleNode("A"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        a2 = new SampleData();
        a2.msi.submissionIdentifier = "A";
        a2.msi.submissionUpdateDate = a1.msi.submissionUpdateDate;
        a2.msi.submissionReleaseDate = a1.msi.submissionReleaseDate;
        try {
            a2.scd.addNode(new SampleNode("A"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        b = new SampleData();
        b.msi.submissionIdentifier = "B";
        try {
            b.scd.addNode(new SampleNode("B"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        //TODO expand to cover many other possible comparisons
    }
    
    public void tearDown() throws Exception{
        a1 = null;
        a2 = null;
        b = null;
    }
    
    public void testCompare(){
        
        assertEquals("less than", -1, comp.compare(a1, b));
        assertEquals("greater than", 1, comp.compare(b, a1));
        assertEquals("equal to", 0, comp.compare(a2, a1));
        assertEquals("equal to", 0, comp.compare(a1, a2));
        
        List<SampleData> sorted = new ArrayList<SampleData>();
        sorted.add(b);
        sorted.add(a1);
        sorted.add(a2);
        Collections.sort(sorted, comp);
        List<SampleData> target = new ArrayList<SampleData>();
        target.add(a1);
        target.add(a2);
        target.add(b);
        
        
        assertEquals("sorted lists equal", target, sorted);
    }
	
}
