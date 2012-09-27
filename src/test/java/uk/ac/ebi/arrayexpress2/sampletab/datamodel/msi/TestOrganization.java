package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class TestOrganization extends TestCase {

    private Organization a1;
    private Organization a2;
    private Organization b;
    
    public void setUp() {
		a1 = new Organization("a", "", "A", "", "");
        a2 = new Organization("a", null, "A", null, null);
        b = new Organization("b", "", "B", null, null);
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

        Set<Organization> testset = new HashSet<Organization>();
        
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
        
        List<Organization> sorted = new ArrayList<Organization>();
        sorted.add(b);
        sorted.add(a1);
        sorted.add(a2);
        Collections.sort(sorted);
        List<Organization> target = new ArrayList<Organization>();
        target.add(a1);
        target.add(a2);
        target.add(b);
        
        
        assertEquals("sorted lists equal", target, sorted);
    }
	
}
