package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Database;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Organization;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Person;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Publication;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.TermSource;

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
		if(msi.submissionIdentifier == null || msi.submissionIdentifier.trim().length() == 0) {
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

        writeOrganizations(msi.organizations);
        writePersons(msi.persons);
        writePublications(msi.publications);
        writeTermSources(msi.termSources);
		writeDatabases(msi.databases);
	}

	public void writeSingleField(String heading, String value)
			throws IOException {
		if (value != null) {
			writer.write(heading);
			writer.write("\t");
            value = SampleTabWriter.sanitize(value);
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
                    value = SampleTabWriter.sanitize(value);
					writer.write(value);
				}
			}
			writer.write("\n");
		}
	}
    
    public void writeOrganizations(Collection<Organization> organizations)
        throws IOException {
        if (organizations != null && organizations.size() > 0) {
            List<Organization> orgs = new ArrayList<Organization>(new HashSet<Organization>(organizations));
            while (orgs.contains(null)) {
                orgs.remove(null);
            }
            writer.write("Organization Name\t");
            for (Organization org : orgs) {
                if (org.getName() != null) {
                    writer.write(org.getName());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Organization Address\t");
            for (Organization org : orgs) {
                if (org.getAddress() != null) {
                    writer.write(org.getAddress());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Organization URI\t");
            for (Organization org : orgs) {
                if (org.getURI() != null) {
                    writer.write(org.getURI());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Organization Email\t");
            for (Organization org : orgs) {
                if (org.getEmail() != null) {
                    writer.write(org.getEmail());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Organization Role\t");
            for (Organization org : orgs) {
                if (org.getRole() != null) {
                    writer.write(org.getRole());
                }
                writer.write("\t");
            }
            writer.write("\n");
        }
    }

    public void writePersons(Collection<Person> persons)
        throws IOException {
        if (persons != null && persons.size() > 0) {
            List<Person> pers = new ArrayList<Person>(new HashSet<Person>(persons));
            while (pers.contains(null)){
                pers.remove(null);
            }
            writer.write("Person Last Name\t");
            for (Person per : pers) {
                if (per.getLastName() != null) {
                    writer.write(per.getLastName());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Person Initials\t");
            for (Person per : pers) {
                if (per.getInitials() != null) {
                    writer.write(per.getInitials());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Person First Name\t");
            for (Person per : pers) {
                if (per.getFirstName() != null) {
                    writer.write(per.getFirstName());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Person Email\t");
            for (Person per : pers) {
                if (per.getEmail() != null) {
                    writer.write(per.getEmail());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Person Role\t");
            for (Person per : pers) {
                if (per.getRole() != null) {
                    writer.write(per.getRole());
                }
                writer.write("\t");
            }
            writer.write("\n");
        }
    }
    
    public void writePublications(Collection<Publication> publications)
        throws IOException {
        if (publications != null && publications.size() > 0) {
            //convert to hashset to remove duplicates
            //convert back so it has consistent order
            Set<Publication> pubset = new HashSet<Publication>();
            pubset.addAll(publications);
            List<Publication> pubs = new ArrayList<Publication>();
            pubs.addAll(pubset);
            while (pubs.contains(null)){
                pubs.remove(null);
            }
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
	
	public void writeTermSources(Collection<TermSource> termsources)
        throws IOException {
	    if (termsources != null && termsources.size() > 0) {
            //remove duplicates with consistent order
            List<TermSource> tss = new ArrayList<TermSource>();
            for (TermSource ts : termsources) {
                if (!tss.contains(ts)) {
                    tss.add(ts);
                }
            }
            while (tss.contains(null)) {
                tss.remove(null);
            }
            
	        writer.write("Term Source Name\t");
	        for (TermSource ts : tss) {
                if (ts.getName() != null && ts.getName().trim().length() > 0) {
                    writer.write(ts.getName());
                    writer.write("\t");
                }
	        }
            writer.write("\n");
            writer.write("Term Source URI\t");
            for (TermSource ts : tss) {
                if (ts.getName() != null && ts.getName().trim().length() > 0) {
                    if (ts.getURI() != null){
                        writer.write(ts.getURI());
                    }
                    writer.write("\t");
                }
            }
            writer.write("\n");
            writer.write("Term Source Version\t");
            for (TermSource ts : tss) {
                if (ts.getName() != null && ts.getName().trim().length() > 0) {
                    if (ts.getVersion() != null){
                        writer.write(ts.getVersion());
                    }
                    writer.write("\t");
                }
            }
            writer.write("\n");
	    }
	}
    
    public void writeDatabases(Collection<Database> databases)
        throws IOException {
        if (databases != null && databases.size() > 0) {
            List<Database> dbs = new ArrayList<Database>(new HashSet<Database>(databases));
            while (dbs.contains(null)) {
                dbs.remove(null);
            }
            writer.write("Database Name\t");
            for (Database db : dbs) {
                if (db.getName() != null) {
                    writer.write(db.getName());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Database ID\t");
            for (Database db : dbs) {
                if (db.getID() != null) {
                    writer.write(db.getID());
                }
                writer.write("\t");
            }
            writer.write("\n");
            writer.write("Database URI\t");
            for (Database db : dbs) {
                if (db.getURI() != null) {
                    writer.write(db.getURI());
                }
                writer.write("\t");
            }
            writer.write("\n");
        }
    }
}
