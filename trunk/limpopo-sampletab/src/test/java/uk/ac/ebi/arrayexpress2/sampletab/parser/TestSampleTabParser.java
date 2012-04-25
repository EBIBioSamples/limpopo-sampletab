package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.mged.magetab.error.ErrorCode;
import org.mged.magetab.error.ErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.UnresolvedPlaceholderNode;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Publication;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.TermSource;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.GroupNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.DatabaseAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.OrganismAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.renderer.SampleTabWriter;

public class TestSampleTabParser extends TestCase {
    private SampleTabParser<SampleData> parser;

    private URL resource;
    private URL resource_broken;
    private URL resource_ae;
    private URL resource_corriel;
    private URL resource_dgva;
    private URL resource_imsr;
    private URL resource_sra;
    private URL resource_groups;

    private List<ErrorItem> errorItems;

    public void setUp() {
        resource_ae = getClass().getClassLoader().getResource("GAE-MEXP-986/sampletab.pre.txt");
        resource_corriel = getClass().getClassLoader().getResource("GCR-autism/sampletab.pre.txt");
        resource_imsr = getClass().getClassLoader().getResource("GMS-HAR/sampletab.pre.txt");
        resource_dgva = getClass().getClassLoader().getResource("GVA-estd1/sampletab.pre.txt");
        resource_sra = getClass().getClassLoader().getResource("GEN-ERP001075/sampletab.pre.txt");
        
        resource = getClass().getClassLoader().getResource("dummy/sampletab.txt");
        resource_broken = getClass().getClassLoader().getResource("broken/sampletab.txt");
        resource_groups = getClass().getClassLoader().getResource("groups/sampletab.txt");
        parser = new SampleTabParser<SampleData>();
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
        SampleData st = null;
        try {
            st = doParse(resource.openStream());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        // check database handler
        SCDNode node = st.scd.getNode("sampleA", SampleNode.class);
        assertNotSame("Node sampleA is not null", null, node);
        String dbname = null;
        SCDNodeAttribute dbattr = null;
        for (SCDNodeAttribute attr : node.getAttributes()) {
            if (attr.getAttributeType().equals("Database Name"))
                dbattr = attr;
        }
        assertNotSame("Attribute DatabaseName is not null", null, dbattr);
        dbname = dbattr.getAttributeValue();
        assertEquals("Check parsed DatabaseName", "bobdb", dbname);
        
        List<Publication> pubs = new ArrayList<Publication>();
        pubs.addAll(st.msi.publications);
        assertEquals("Check PubMedID", "666", pubs.get(0).getPubMedID());
        
        Set<TermSource> tss = new HashSet<TermSource>();
        tss.addAll(st.msi.termSources);
        assertEquals("Check term source similarity", 1, tss.size());

        // check submission reference layer handler
        assertSame("Submission Reference Layer", true, st.msi.submissionReferenceLayer);
    }
    
    public void testGroups() {

        SampleData st = null;
        try {
            st = doParse(resource_groups.openStream());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals("Number of groups", 4, st.scd.getNodes(GroupNode.class).size());
        
    }

    public void testParseAE() {
        try {
            SampleData st = doParse(resource_ae.openStream());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testParseSRA() {
        SampleData st = null;
        try {
            st = doParse(resource_sra.openStream());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            fail();
            return;
        }
        
        if (st != null){
            for (SampleNode s : st.scd.getNodes(SampleNode.class)){
                boolean hasDatabase = false;
                for (SCDNodeAttribute a : s.getAttributes()){
                    if (DatabaseAttribute.class.isInstance(a)){
                        hasDatabase = true;
                    }
                }
                assertTrue(hasDatabase);
            }
            
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
            //System.out.println(sw.toString());
        }
    }

    public void testParseBroken() {
        try {
            SampleData st = doParse(resource_broken.openStream());
            fail(); // if exception not thrown
        } catch (ParseException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testParseCorriel() {
        try {
            SampleData st = doParse(resource_corriel.openStream());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

//    public void testParseIMSR() {
//        try {
//            SampleData st = doParse(resource_imsr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            fail();
//        } catch (IOException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

    public void testParseDGVa() {
        SampleData st = null;
        try {
            st = doParse(resource_dgva.openStream());
//            BufferedWriter tmplog = new BufferedWriter(new OutputStreamWriter(System.out));
//            SampleTabWriter w = new SampleTabWriter(tmplog);
//            w.write(st);
//            tmplog.flush();
//            tmplog.close();
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        GroupNode s = st.scd.getNode("G1", GroupNode.class);
        SCDNodeAttribute a = s.getAttributes().get(0);
        assertTrue(OrganismAttribute.class.isInstance(a));
        
        OrganismAttribute o = (OrganismAttribute) a;
        
        assertEquals("9606", o.getTermSourceID());
        assertEquals("NCBI Taxonomy", o.getTermSourceREF());
        
    }

    private SampleData doParse(InputStream is) throws ParseException, IOException {
        SampleData sampledata = null;
        sampledata = parser.parse(is);
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
                System.err.println(sb);
            }
            throw new ParseException();
        }

        // check msi title is not null
        assertNotNull("SubmissionTitle must not be null", sampledata.msi.submissionTitle);
        assertNotNull("SubmissionIdentifier must not be null", sampledata.msi.submissionIdentifier);
        assertNotSame("SubmissionTitle should not be an empty string", "", sampledata.msi.submissionTitle);
        assertNotSame("Submission Release Date should not be null", "", sampledata.msi.submissionReleaseDate);
        assertNotSame("Submission Release Date should not be blank", "",
                sampledata.msi.getSubmissionReleaseDateAsString());

        assertNotSame("SCD node count", 0, sampledata.scd.getNodeCount());
        ArrayList<SCDNode> nodes = new ArrayList<SCDNode>(sampledata.scd.getNodes("samplename"));
        assertNotSame("SCD nodes.size()", 0, nodes.size());
        assertNotNull("SCD node by index", nodes.get(0));
        SCDNode node = nodes.get(0);
        assertNotSame("SCD node attribute count", 0, node.getAttributes().size());
        
        for (SCDNode node1 : sampledata.scd.getAllNodes()){
            assertNotSame("Found an UnresolvedPlaceholderNode", UnresolvedPlaceholderNode.class, node1.getClass());
        }
        
        //check it can be written out again
        SampleTabWriter w = new SampleTabWriter(new StringWriter());
        w.write(sampledata);
        return sampledata;
    }

}
