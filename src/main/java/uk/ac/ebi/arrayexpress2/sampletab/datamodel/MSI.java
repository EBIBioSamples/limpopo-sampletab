package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Database;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Organization;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Person;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Publication;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.TermSource;

public class MSI {
	
	public volatile String submissionTitle = null;
	public volatile String submissionDescription = null;
	public volatile String submissionIdentifier = null;
	public volatile String submissionVersion = "1.0";
	public volatile Date submissionReleaseDate = null;
	public volatile Date submissionUpdateDate = null;
	public volatile Boolean submissionReferenceLayer = false;

	//these have to be created as lists so the parsing works correctly
	//however, may be converted to sets on writing
	public List<Organization> organizations = new ArrayList<Organization>();
	public List<Person> persons = new ArrayList<Person>();
    public List<TermSource> termSources = new ArrayList<TermSource>();
	public List<Publication> publications = new ArrayList<Publication>();
	public List<Database> databases = new ArrayList<Database>();

	private Logger log = LoggerFactory.getLogger(getClass());
	
	protected Logger getLog() {
		return log;
	}
	
	//convenience methods
	public String getSubmissionReleaseDateAsString(){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
	    if (this.submissionReleaseDate == null) {
	        log.warn("submissionReleaseDate is null");
	        return simpledateformat.format(new Date());
	    }
		return simpledateformat.format(this.submissionReleaseDate);
	}
	
	public String getSubmissionUpdateDateAsString(){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
		if (this.submissionUpdateDate == null){
		    return this.getSubmissionReleaseDateAsString();
		}
		return simpledateformat.format(this.submissionUpdateDate);
	}
}
