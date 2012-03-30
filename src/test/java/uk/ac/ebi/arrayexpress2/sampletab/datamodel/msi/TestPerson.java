package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class TestPerson extends TestCase {

    private Person a1;
    private Person a2;
    private Person b;
    private Set<Person> testset; 
    
    public void setUp() {
		a1 = new Person("a", "", "A", "", "");
        a2 = new Person("a", null, "A", null, null);
        b = new Person("b", "", "B", null, null);
        testset = new HashSet<Person>();
    }
    
    public void tearDown() throws Exception{
        a1 = null;
        a2 = null;
        b = null;
    }
    
    public void testTermSource(){
        assertTrue("comparable", a1.equals(a2));
        assertTrue("comparable", a2.equals(a1));
        assertFalse("different", a1.equals(b));
        assertFalse("different", b.equals(a1));
        
        assertTrue("hash comparable", a1.hashCode() == a2.hashCode());
        //collisions are possible, but not desirable
        //assertFalse("hash different", a1.hashCode() != b.hashCode());
        
        testset.add(a1);
        assertTrue("same objects contains", testset.contains(a1));
        assertTrue("equal objects contains", testset.contains(a2));
        testset.add(a2);
        testset.add(b);
        assertEquals("Sets ignore equal", 2, testset.size());
        
    }
	
}
