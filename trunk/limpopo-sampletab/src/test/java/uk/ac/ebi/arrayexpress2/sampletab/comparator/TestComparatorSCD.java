package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.comparator.ComparatorSCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;

import junit.framework.TestCase;

public class TestComparatorSCD extends TestCase {

    private SCD a1;
    private SCD a2;
    private SCD b;
    private ComparatorSCD comp = new ComparatorSCD(true);
    
    public void setUp() {
		a1 = new SCD();
		try {
            a1.addNode(new SampleNode("A"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        a2 = new SCD();
        try {
            a2.addNode(new SampleNode("A"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        b = new SCD();
        try {
            b.addNode(new SampleNode("B"));
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
        assertEquals("equal to", 0, comp.compare(a1, a2));
        
        List<SCD> sorted = new ArrayList<SCD>();
        sorted.add(b);
        sorted.add(a1);
        sorted.add(a2);
        Collections.sort(sorted, comp);
        List<SCD> target = new ArrayList<SCD>();
        target.add(a1);
        target.add(a2);
        target.add(b);
        
        
        assertEquals("sorted lists equal", target, sorted);
    }
	
}
