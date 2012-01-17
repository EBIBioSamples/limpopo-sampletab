package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.mged.magetab.error.ErrorCode;
import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

public class TestSampleTabParser extends TestCase {
    private SampleTabParser<SampleData> parser;

    private OutputStream outputStream;

    private URL resource;
    private URL resource_ae;
    private URL resource_corriel;
    private URL resource_imsr;

    private List<ErrorItem> errorItems = new ArrayList<ErrorItem>();

    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        resource_ae = getClass().getClassLoader().getResource("GAE-MEXP-986/sampletab.pre.txt");
        resource_corriel = getClass().getClassLoader().getResource("GCR-autism/sampletab.pre.txt");
        resource_imsr = getClass().getClassLoader().getResource("GMS-HAR/sampletab.pre.txt");
        resource = getClass().getClassLoader().getResource("dummy/sampletab.txt");

        parser = new SampleTabParser<SampleData>();
        parser.addErrorItemListener(new ErrorItemListener() {
            public void errorOccurred(ErrorItem item) {
                errorItems.add(item);
            }
        });
        
    }

    public void tearDown() {
        parser = null;
        errorItems = null;
    }

    public void testParse() {
        doParse(resource);
    }
    
    //this causes the fail
    public void testParseAE() {
        doParse(resource_ae);
    }
    
    public void testParseCorriel() {
        doParse(resource_corriel);
    }
    
    //this also causes fail
    public void testParseIMSR() {
        doParse(resource_imsr);
    }
    
    private void doParse(URL url){
        try {
            SampleData sampledata = parser.parse(url);
            if (!errorItems.isEmpty()) {
                // there are error items, print them and fail
                StringBuilder sb = new StringBuilder();
                for (ErrorItem item : errorItems) {
                    ErrorCode code = null;
                    for (ErrorCode ec : ErrorCode.values()) {
                        if (item.getErrorCode() == ec.getIntegerValue()) {
                            code = ec;
                            break;
                        }
                    }

                    if (code != null) {
                        sb.append("Listener reported error...").append("\n");
                        sb.append("\tError Code: ")
                                .append(item.getErrorCode())
                                .append(" [")
                                .append(code.getErrorMessage())
                                .append("]").append("\n");
                        sb.append("\tType: ").append(item.getErrorType()).append("\n");
                        sb.append("\tFile: ").append(item.getParsedFile()).append("\n");
                        sb.append("\tLine: ").append(item.getLine() != -1 ? item.getLine() : "n/a").append("\n");
                        sb.append("\tColumn: ").append(item.getCol() != -1 ? item.getCol() : "n/a").append("\n");
                        sb.append("\tAdditional comment: ").append(item.getComment()).append("\n");
                    }
                    else {
                        sb.append("Listener reported error...");
                        sb.append("\tError Code: ").append(item.getErrorCode()).append("\n");
                        sb.append("\tFile: ").append(item.getParsedFile()).append("\n");
                        sb.append("\tLine: ").append(item.getLine() != -1 ? item.getLine() : "n/a").append("\n");
                        sb.append("\tColumn: ").append(item.getCol() != -1 ? item.getCol() : "n/a").append("\n");
                        sb.append("\tAdditional comment: ").append(item.getComment()).append("\n");
                    }
                    sb.append("\n");
                }
                fail("The parser generated the following error items:\n" + sb.toString());
            }

            // check msi title is not null
            assertNotNull("SubmissionTitle must not be null", sampledata.msi.submissionTitle);
            assertNotSame("SubmissionTitle should not be an empty string", 
                          "", sampledata.msi.submissionTitle);
            
            assertNotSame("SCD node count should not be zero", 0, sampledata.scd.getNodeCount());
            ArrayList<SCDNode> nodes = new ArrayList<SCDNode>(sampledata.scd.getNodes("samplename"));
            assertNotSame("SCD node count should not be zero", 0, nodes.size());
            assertNotNull("SCD node should be getable by index", nodes.get(0));
            SCDNode node = nodes.get(0);
            assertNotSame("SCD node attribute count should not be zero", 0, node.getAttributes().size());
        }
        catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
    }

}
