package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class TestMSI extends TestCase {

    private MSI a1;
    private MSI a2;
    private MSI b;
    
    public void setUp() {
		a1 = new MSI();
		a1.submissionIdentifier = "A";
        a2 = new MSI();
        a2.submissionIdentifier = "A";
        a2.submissionUpdateDate = a1.submissionUpdateDate;
        a2.submissionReleaseDate = a1.submissionReleaseDate;
        b = new MSI();
        b.submissionIdentifier = "B";
        //TODO expand to cover many other possible comparisons
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

        Set<MSI> testset = new HashSet<MSI>();
        
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
        
        List<MSI> sorted = new ArrayList<MSI>();
        sorted.add(b);
        sorted.add(a1);
        sorted.add(a2);
        Collections.sort(sorted);
        List<MSI> target = new ArrayList<MSI>();
        target.add(a1);
        target.add(a2);
        target.add(b);
        
        
        assertEquals("sorted lists equal", target, sorted);
    }
	
}
