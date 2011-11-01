package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.parser.SampleTabParser;
import junit.framework.TestCase;

public class TestSampleTabRenderer extends TestCase {
    private SampleTabParser<SampleData> parser;
    private SampleTabWriter sampletabwriter;
    private SampleData sampledata;
    private StringWriter out;
    private URL resource;

    public void setUp() {
        //resource = getClass().getClassLoader().getResource("GAE-MEXP-986/sampletab.pre.txt");
    	resource = getClass().getClassLoader().getResource("GCR-ninds/sampletab.pre.txt");
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
}
