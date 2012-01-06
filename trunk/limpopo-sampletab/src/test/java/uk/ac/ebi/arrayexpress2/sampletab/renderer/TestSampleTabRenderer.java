package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;
import uk.ac.ebi.arrayexpress2.sampletab.parser.SampleTabParser;
import junit.framework.TestCase;

public class TestSampleTabRenderer extends TestCase {
    private SampleTabParser<SampleData> parser;
    private SampleTabWriter sampletabwriter;
    private SampleData sampledata;
    private StringWriter out;
    private URL resource;

    public void setUp() {
        resource = getClass().getClassLoader().getResource("GAE-MEXP-986/sampletab.pre.txt");
    	//resource = getClass().getClassLoader().getResource("GCR-ninds/sampletab.pre.txt");
        parser = new SampleTabParser<SampleData>();
        try {
			sampledata = parser.parse(resource);
		} catch (ParseException e) {
            e.printStackTrace();
            fail();
		}
		out = new StringWriter();
    }
    
    public void tearDown() throws Exception{
        super.tearDown();
        out = null;
        sampletabwriter = null;
    }
//
//	public void testRender() {
//		sampletabwriter = new SampleTabWriter(out);
//		try {
//			sampletabwriter.write(sampledata);
//            //System.out.println(out.toString());
//		} catch (IOException e) {
//            e.printStackTrace();
//            fail();
//		}
//	}
	
	public void testDerrivedFrom(){
		SampleData st = new SampleData();
		
		SampleNode parent = new SampleNode();
		parent.setNodeName("parent");
		
		SampleNode child = new SampleNode();
		child.setNodeName("child");
		child.addParentNode(parent);
		parent.addChildNode(child);
		
		try {
			st.scd.addNode(parent);
		} catch (ParseException e) {
            e.printStackTrace();
            fail();
		}  
		
		BufferedWriter screen = new BufferedWriter(new OutputStreamWriter(System.out));

		SampleTabWriter sampletabwriterscreen = new SampleTabWriter(screen);
		try {
			sampletabwriterscreen.write(st);
		} catch (IOException e) {
            e.printStackTrace();
            fail();
		}
		
//		try {
//			sampletabwriterscreen.flush();
//		} catch (IOException e) {
//            e.printStackTrace();
//            fail();
//		}
	}
}
