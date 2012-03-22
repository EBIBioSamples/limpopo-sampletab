package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Database;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Publication;

public class MSIWriter extends Writer {
	private Writer writer;

	private Logger log = LoggerFactory.getLogger(getClass());

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
		if(msi.submissionIdentifier == null || msi.submissionIdentifier.trim().length() == 0){
		    log.error("Submission Identifier is invalid");
		}
		writeSingleField("Submission Identifier", msi.submissionIdentifier);
		writeSingleField("Submission Description", msi.submissionDescription);
		writeSingleField("Submission Version", msi.submissionVersion);
		if (msi.submissionReferenceLayer == null) {
			// do nothing
		} else if (msi.submissionReferenceLayer) {
			writeSingleField("Submission Reference Layer", "true");
		} else {
			writeSingleField("Submission Reference Layer", "false");
		}
		writeSingleField("Submission Release Date",
				msi.getSubmissionReleaseDateAsString());
		writeSingleField("Submission Update Date",
				msi.getSubmissionUpdateDateAsString());
		
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
        writePublications(msi.publications);
		writeMultiField("Term Source Name", msi.termSourceName);
		writeMultiField("Term Source URI", msi.termSourceURI);
		writeMultiField("Term Source Version", msi.termSourceVersion);
		writeDatabases(msi.databases);
	}

	public void writeSingleField(String heading, String value)
			throws IOException {
		if (value != null) {
			writer.write(heading);
			writer.write("\t");
			writer.write(value);
			writer.write("\n");
		}
	}

	public void writeMultiField(String heading, List<String> values)
			throws IOException {
		if (values != null) {
			writer.write(heading);
			for (String value : values) {
				writer.write("\t");
				if (value != null) {
                    //purge all stange characters
				    value = value.replace("\"", "");
                    value = value.replace("\n", "");
                    value = value.replace("\t", "");
					writer.write(value);
				}
			}
			writer.write("\n");
		}
	}
	
	public void writeDatabases(Collection<Database> databases)
        throws IOException {
	    if (databases != null){
	        List<Database> dbs = new ArrayList<Database>(databases);
	        writer.write("Database Name\t");
	        for (Database db : dbs){
                if (db.getName() != null){
                    writer.write(db.getName());
                }
                writer.write("\t");
	        }
            writer.write("\n");
            writer.write("Database ID\t");
            for (Database db : dbs){
                if (db.getID() != null){
                    writer.write(db.getID());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Database URI\t");
            for (Database db : dbs){
                if (db.getURI() != null){
                    writer.write(db.getURI());
                }
                writer.write("\t");
            }
            writer.write("\n");
	    }
	}
    
    public void writePublications(Collection<Publication> publications)
        throws IOException {
        if (publications != null){
            List<Publication> pubs = new ArrayList<Publication>(publications);
            writer.write("Publication PubMed ID\t");
            for (Publication pub : pubs){
                if (pub.getPubMedID() != null){
                    writer.write(pub.getPubMedID());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Publication DOI\t");
            for (Publication pub : pubs){
                if (pub.getDOI() != null){
                    writer.write(pub.getDOI());
                }
                writer.write("\t");
            }
            writer.write("\n");
        }
    }
}
