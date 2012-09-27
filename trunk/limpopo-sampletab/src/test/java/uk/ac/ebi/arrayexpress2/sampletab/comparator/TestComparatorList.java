package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestComparatorList extends TestCase {

    private List<String> a1;
    private List<String> a2;
    private List<String> b;
    private List<String> d;
    private ComparatorList<String> comp;
    
    public void setUp() {
		a1 = new ArrayList<String>();
		a1.add("A");
        a1.add("B");
        a1.add("C");
        a2 = new ArrayList<String>();
        a2.add("A");
        a2.add("B");
        a2.add("C");
        
        b = new ArrayList<String>();
        b.add("X");
        b.add("Y");
        b.add("Z");
        
        d = new ArrayList<String>();
        d.add("M");
        d.add("N");
        
        comp = new ComparatorList<String>();
    }
    
    public void tearDown() throws Exception{
        a1 = null;
        a2 = null;
        b = null;
    }
        
    public void testCompareTo(){
        assertTrue("less than", -1 >= comp.compare(a1, b));
        assertTrue("greater than", 1 <= comp.compare(b, a1));
        assertEquals("less than", 0, comp.compare(a1, a2));
        assertTrue("greater than", 1 <= comp.compare(a1, d));
    }
	
}
