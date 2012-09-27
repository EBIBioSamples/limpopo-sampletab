package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

public class TestComparatorSortedSet extends TestCase {

    private SortedSet<String> a1;
    private SortedSet<String> a2;
    private SortedSet<String> b;
    private SortedSet<String> d;
    private ComparatorSortedSet<String> comp;
    
    public void setUp() {
		a1 = new TreeSet<String>();
		a1.add("A");
        a1.add("B");
        a1.add("C");
        a2 = new TreeSet<String>();
        a2.add("A");
        a2.add("B");
        a2.add("C");
        
        b = new TreeSet<String>();
        b.add("X");
        b.add("Y");
        b.add("Z");
        
        d = new TreeSet<String>();
        d.add("M");
        d.add("N");
        
        comp = new ComparatorSortedSet<String>();
    }
    
    public void tearDown() throws Exception{
        a1 = null;
        a2 = null;
        b = null;
    }
        
    public void testCompare(){
        assertTrue("less than", -1 >= comp.compare(a1, b));
        assertTrue("greater than", 1 <= comp.compare(b, a1));
        assertEquals("less than", 0, comp.compare(a1, a2));
        assertTrue("greater than", 1 <= comp.compare(a1, d));
    }
	
}
