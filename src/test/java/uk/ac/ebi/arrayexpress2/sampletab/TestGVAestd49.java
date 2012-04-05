package uk.ac.ebi.arrayexpress2.sampletab;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mged.magetab.error.ErrorCode;
import org.mged.magetab.error.ErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.parser.SampleTabParser;
import uk.ac.ebi.arrayexpress2.sampletab.renderer.SampleTabWriter;

import junit.framework.TestCase;

public class TestGVAestd49 extends TestCase {

    private URL resource;
    private SampleTabParser<SampleData> parser;
    private List<ErrorItem> errorItems;
    private SampleTabWriter writer;

    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    public void setUp() {
        resource = getClass().getClassLoader().getResource("GVA-estd49/sampletab.pre.txt");
        parser = new SampleTabParser<SampleData>();
        errorItems = new ArrayList<ErrorItem>();
        parser.addErrorItemListener(new ErrorItemListener() {
            public void errorOccurred(ErrorItem item) {
                errorItems.add(item);
            }
        });
    }
    
    public void tearDown() {
        resource = null;
        parser = null;
        errorItems = null;
    }
    
    private SampleData parse(URL resource){
        SampleData st = null;
        try {
            st = parser.parse(resource);
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
            return null;
        }
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
                    sb.append("\tError Code: ").append(item.getErrorCode()).append(" [").append(code.getErrorMessage())
                            .append("]").append("\n");
                    sb.append("\tType: ").append(item.getErrorType()).append("\n");
                    sb.append("\tFile: ").append(item.getParsedFile()).append("\n");
                    sb.append("\tLine: ").append(item.getLine() != -1 ? item.getLine() : "n/a").append("\n");
                    sb.append("\tColumn: ").append(item.getCol() != -1 ? item.getCol() : "n/a").append("\n");
                    sb.append("\tAdditional comment: ").append(item.getComment()).append("\n");
                } else {
                    sb.append("Listener reported error...");
                    sb.append("\tError Code: ").append(item.getErrorCode()).append("\n");
                    sb.append("\tFile: ").append(item.getParsedFile()).append("\n");
                    sb.append("\tLine: ").append(item.getLine() != -1 ? item.getLine() : "n/a").append("\n");
                    sb.append("\tColumn: ").append(item.getCol() != -1 ? item.getCol() : "n/a").append("\n");
                    sb.append("\tAdditional comment: ").append(item.getComment()).append("\n");
                }
                sb.append("\n");
                log.error(sb.toString());
            }
            fail();
            return null;
        }
        return st;
    }
    
    
    public void testRoundTrip() {

        SampleData st = parse(resource);
        assertFalse(st == null);
        
        SampleTabWriter w;
        StringWriter sw = new StringWriter();
        w = new SampleTabWriter(sw);
        try {
            w.write(st);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        System.out.println(sw.toString());
        //SampleData st2 = parse(sw.toString());
    }
}
