package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class TestPerson extends TestCase {

    private Person a1;
    private Person a2;
    private Person b;
    
    public void setUp() {
		a1 = new Person("a", "", "A", "", "");
        a2 = new Person("a", null, "A", null, null);
        b = new Person("b", "", "B", null, null);
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

        Set<Person> testset = new HashSet<Person>();
        
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
        
        List<Person> personsSort = new ArrayList<Person>();
        personsSort.add(b);
        personsSort.add(a1);
        personsSort.add(a2);
        Collections.sort(personsSort);
        List<Person> personsTarget = new ArrayList<Person>();
        personsTarget.add(a1);
        personsTarget.add(a2);
        personsTarget.add(b);
        
        
        assertEquals("sorted lists equal", personsTarget, personsSort);
    }
	
}
