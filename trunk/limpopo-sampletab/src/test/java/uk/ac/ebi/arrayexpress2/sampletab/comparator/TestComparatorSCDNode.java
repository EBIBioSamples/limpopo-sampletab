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
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;

import junit.framework.TestCase;

public class TestComparatorSCDNode extends TestCase {

    private SCDNode a1;
    private SCDNode a2;
    private SCDNode b;
    private ComparatorSCDNode comp = new ComparatorSCDNode(true);
    
    public void setUp() {
		a1 = new SampleNode("A");
        a2 = new SampleNode("A");
        b = new SampleNode("B");
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
        
        List<SCDNode> sorted = new ArrayList<SCDNode>();
        sorted.add(b);
        sorted.add(a1);
        sorted.add(a2);
        Collections.sort(sorted, comp);
        List<SCDNode> target = new ArrayList<SCDNode>();
        target.add(a1);
        target.add(a2);
        target.add(b);        
        
        assertEquals("sorted lists equal", target, sorted);
    }
	
}
