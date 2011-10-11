package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.mged.magetab.error.ErrorCode;
import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class TestSampleTabParser extends TestCase {
    private SampleTabParser parser;
    private SampleTabParser<OutputStream> writingParser;

    private OutputStream outputStream;

    private URL resource;

    private List<ErrorItem> errorItems = new ArrayList<ErrorItem>();

    public void setUp() {
        parser = new SampleTabParser();
        outputStream = new ByteArrayOutputStream();
        writingParser = new SampleTabParser<OutputStream>(outputStream);
        resource = getClass().getClassLoader().getResource("GAE-MEXP-986/sampletab.pre.txt");

        errorItems = new ArrayList<ErrorItem>();

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
        try {
            System.out.println("Starting parsing...");
            SampleData sampledata = parser.parse(resource);

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
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
