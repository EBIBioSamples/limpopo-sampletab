package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.UnresolvedPlaceholderNode;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.CommentAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.parser.SampleTabParser;
import junit.framework.TestCase;

public class TestSampleTabRenderer extends TestCase {
    private SampleTabWriter sampletabwriter;
    private SampleData sampledata;
    private StringWriter out;
    private URL resource;

    public void setUp() {
		out = new StringWriter();
		//create a dummy file content to use
		sampledata = new SampleData();
        sampledata.msi.submissionTitle = "dummy";
        
        SampleNode parent = new SampleNode();
        parent.setNodeName("parent");
        CommentAttribute comment = new CommentAttribute();
        comment.type = "stuff";
        comment.setAttributeValue("stuff");
        parent.addAttribute(comment);
        
        SampleNode child = new SampleNode();
        child.setNodeName("child");
        comment = new CommentAttribute();
        comment.type = "morestuff";
        comment.setAttributeValue("morestuff");
        child.addAttribute(comment);

        child.addParentNode(parent);
        parent.addChildNode(child);
        
        try {
            sampledata.scd.addNode(parent);
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }  
        
        parent = new SampleNode();
        parent.setNodeName("nonparent");
        try {
            sampledata.scd.addNode(parent);
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }  
		
    }
    
    public void tearDown() throws Exception{
        super.tearDown();
        out = null;
        sampletabwriter = null;
    }

    public void testUnresolvedNode(){
        String type = "samplename";
        String value =  "bar";
        UnresolvedPlaceholderNode unresolved = new UnresolvedPlaceholderNode(type, value);
        SampleNode sample = new SampleNode();
        sample.setNodeName(value);

        //System.out.println("unresolved.getNodeType() == "+unresolved.getNodeType());
        //System.out.println("unresolved.getNodeName() == "+unresolved.getNodeName());
        //System.out.println("sample.getNodeType() == "+sample.getNodeType());
        //System.out.println("sample.getNodeName() == "+sample.getNodeName());
        
        assertTrue("nodes unequal", unresolved.equals(sample));
        assertTrue("nodes unequal reversed", sample.equals(unresolved));
        assertTrue("node hashes unequal", unresolved.hashCode() == sample.hashCode());
        
        HashMap<Node, Set<Node>> hashmap = new HashMap<Node, Set<Node>>();
        hashmap.put(unresolved, new HashSet<Node>());
        
        assertTrue("unresolved is not in hashmap keys", hashmap.containsKey(unresolved));
        
        assertTrue("sample is not in hashmap keys", hashmap.containsKey(sample));
        
    }
    
	public void testRender() {
		sampletabwriter = new SampleTabWriter(out);
		try {
			sampletabwriter.write(sampledata);
            //System.out.println(out.toString());
		} catch (IOException e) {
            e.printStackTrace();
            fail();
		}
	}
	
	public void testDerrivedFrom(){
		
		BufferedWriter screen = new BufferedWriter(new OutputStreamWriter(System.out));

		SampleTabWriter sampletabwriterscreen = new SampleTabWriter(screen);
		try {
			sampletabwriterscreen.write(sampledata);
            //sampletabwriterscreen.flush();
		} catch (IOException e) {
            e.printStackTrace();
            fail();
		}
		
	}
	
}
