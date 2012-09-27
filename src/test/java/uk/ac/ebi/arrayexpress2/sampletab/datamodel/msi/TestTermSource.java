package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class TestTermSource extends TestCase {

    private TermSource a1;
    private TermSource a2;
    private TermSource b;
    
    public void setUp() {
		a1 = new TermSource("a", "666", "");
        a2 = new TermSource("a", "666", null);
        b = new TermSource("b", "555", null);
    }
    
    public void tearDown() throws Exception{
        a1 = null;
        a2 = null;
        b = null;
    }
    
    public void testEquals(){
        assertTrue("comparable", a1.equals(a2));
        assertTrue("comparable", a2.equals(a1));
        assertFalse("different", a1.equals(b));
        assertFalse("different", b.equals(a1));    
    }
    
    public void testHashCode(){
        assertTrue("hash comparable", a1.hashCode() == a2.hashCode());
        //collisions are possible, but not desirable
        //assertFalse("hash different", a1.hashCode() != b.hashCode());

        Set<TermSource> testset = new HashSet<TermSource>();
        testset.add(a1);
        assertTrue("same objects contains", testset.contains(a1));
        assertTrue("equal objects contains", testset.contains(a2));
        testset.add(a2);
        testset.add(b);
        assertEquals("sets ignore equal", 2, testset.size());
        
    }
    
    public void testCompareTo(){
        
        assertEquals("less than", -1, a1.compareTo(b));
        assertEquals("greater than", 1, b.compareTo(a1));
        assertEquals("equal to", 0, a1.compareTo(a2));
        
        List<TermSource> termSourceSort = new ArrayList<TermSource>();
        termSourceSort.add(b);
        termSourceSort.add(a1);
        termSourceSort.add(a2);
        Collections.sort(termSourceSort);
        List<TermSource> termSourceTarget = new ArrayList<TermSource>();
        termSourceTarget.add(a1);
        termSourceTarget.add(a2);
        termSourceTarget.add(b);
        
        
        assertEquals("sorted lists equal", termSourceTarget, termSourceSort);
    }
	
}
