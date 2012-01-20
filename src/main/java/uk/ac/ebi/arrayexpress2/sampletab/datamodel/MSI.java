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
	
	public volatile String submissionTitle = "";
	public volatile String submissionDescription = "";
	public volatile String submissionIdentifier = "";
	public volatile String submissionVersion = "1.0";
	public volatile Date submissionReleaseDate = null;
	public volatile Date submissionUpdateDate = null;
	public volatile Boolean submissionReferenceLayer = false;

	public volatile List<String> organizationName = new ArrayList<String>();
	public volatile List<String> organizationAddress = new ArrayList<String>();
	public volatile List<String> organizationURI = new ArrayList<String>();
	public volatile List<String> organizationEmail = new ArrayList<String>();
	public volatile List<String> organizationRole = new ArrayList<String>();

	public volatile List<String> personLastName = new ArrayList<String>();
	public volatile List<String> personInitials = new ArrayList<String>();
	public volatile List<String> personFirstName = new ArrayList<String>();
	public volatile List<String> personEmail = new ArrayList<String>();
	public volatile List<String> personRole = new ArrayList<String>();

	public volatile List<String> publicationDOI = new ArrayList<String>();
	public volatile List<String> publicationPubMedID = new ArrayList<String>();

	public volatile List<String> termSourceName = new ArrayList<String>();
	public volatile List<String> termSourceURI = new ArrayList<String>();
	public volatile List<String> termSourceVersion = new ArrayList<String>();

	public volatile List<String> databaseName = new ArrayList<String>();
	public volatile List<String> databaseURI = new ArrayList<String>();
	public volatile List<String> databaseID = new ArrayList<String>();

	//TODO finish implementing collection-based substructures
//	public volatile Collection<Organization> organizations = new LinkedHashSet<Organization>();
//	public volatile Collection<Person> persons = new LinkedHashSet<Person>();
//	public volatile Collection<Publication> publications = new LinkedHashSet<Publication>();
//	public volatile Collection<TermSource> termSources = new LinkedHashSet<TermSource>();
//	public volatile Collection<Database> databases = new LinkedHashSet<Database>();

	private Logger log = LoggerFactory.getLogger(getClass());
	
	protected Logger getLog() {
		return log;
	}
	
	//convenience methods
	public String getSubmissionReleaseDateAsString(){
		if (this.submissionReleaseDate == null){
			return "";
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
		return simpledateformat.format(this.submissionReleaseDate);
	}
	
	public String getSubmissionUpdateDateAsString(){
		if (this.submissionUpdateDate == null){
			return "";
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
		return simpledateformat.format(this.submissionUpdateDate);
	}
}
