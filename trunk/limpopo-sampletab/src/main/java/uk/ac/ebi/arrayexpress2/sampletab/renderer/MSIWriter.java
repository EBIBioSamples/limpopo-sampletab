package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class MSIWriter extends Writer {
    private Writer writer;

    private Logger log = LoggerFactory.getLogger(getClass());
    
	private SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");

	public MSIWriter(Writer writer) {
        this.writer = writer;
	}

    protected Logger getLog() {
        return log;
    }

	@Override
    public void flush() throws IOException {
        writer.flush();
    }

	@Override
    public void close() throws IOException {
        writer.close();
    }

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	}
    
    public void write(MSI msi) throws IOException {
    	writer.write("[MSI]\n");
    	writeSingleField("Submission Title", msi.submissionTitle);
    	writeSingleField("Submission Identifier", msi.submissionIdentifier);
    	writeSingleField("Submission Description", msi.submissionDescription);
    	writeSingleField("Submission Version", msi.submissionVersion);
    	if (msi.submissionReferenceLayer == null){
    		//do nothing
    	} else if (msi.submissionReferenceLayer){
        	writeSingleField("Submission Reference Layer", "true");    		
    	} else {
        	writeSingleField("Submission Reference Layer", "false");
    	}
    	writeSingleField("Submission Release Date", simpledateformat.format(msi.submissionReleaseDate));
    	writeSingleField("Submission Update Date", simpledateformat.format(msi.submissionUpdateDate));
    	writeMultiField("Organization Name", msi.organizationName);
    	writeMultiField("Organization Address", msi.organizationAddress);
    	writeMultiField("Organization URI", msi.organizationURI);
    	writeMultiField("Organization Email", msi.organizationEmail);
    	writeMultiField("Organization Role", msi.organizationRole);
    	writeMultiField("Person Last Name", msi.personLastName);
    	writeMultiField("Person Initials", msi.personInitials);
    	writeMultiField("Person First Name", msi.personFirstName);
    	writeMultiField("Person Email", msi.personEmail);
    	writeMultiField("Person Role", msi.personRole);
    	writeMultiField("Publication DOI", msi.publicationDOI);
    	writeMultiField("Publication PubMed ID", msi.publicationPubMedID);
    	writeMultiField("Term Source Name", msi.termSourceName);
    	writeMultiField("Term Source URI", msi.termSourceURI);
    	writeMultiField("Term Source Version", msi.termSourceVersion);
    	writeMultiField("Database Name", msi.databaseName);
    	writeMultiField("Database URI", msi.databaseURI);
    	writeMultiField("Database ID", msi.databaseID);
    }
    
    public void writeSingleField(String heading, String value) throws IOException {
    	if (value != null) {
	    	writer.write(heading);
	    	writer.write("\t");
	    	writer.write(value);
	    	writer.write("\n");
    	}
    }
    
    public void writeMultiField(String heading, List<String> values) throws IOException {
    	if (values != null) {
	    	writer.write(heading);
	    	for (String value : values){
	        	writer.write("\t");
	        	if (value != null) {
	        		writer.write(value);
	        	}
	    	}
	    	writer.write("\n");
    	}
    }
}

