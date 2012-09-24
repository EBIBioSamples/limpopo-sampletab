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

/**
 * Meta-Submission Information in a SampleTab File.
 * 
 * Information relating to the submission can be altered directly in an MSI object.
 * 
 * Other MSI objects are stored as public lists.
 * 
 * 
 * Read from a SampleTab file by {@Link uk.ac.ebi.arrayexpress2.sampletab.parser.MSIParser} and written out
 * by {@link uk.ac.ebi.arrayexpress2.renderer.MSIWriter}.
 */
public class MSI {
	
	public volatile String submissionTitle = null;
	public volatile String submissionDescription = null;
	public volatile String submissionIdentifier = null;
	public volatile String submissionVersion = "1.2";
	public volatile Date submissionReleaseDate = new Date();
	public volatile Date submissionUpdateDate = new Date();
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
	
	/**
	 * Returns the submission release date in a yyy/MM/dd format, or 
	 * the current date if no release date is specified. 
	 * 
	 *  @return String
	 */
	public String getSubmissionReleaseDateAsString(){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
	    if (this.submissionReleaseDate == null) {
	        log.warn("submissionReleaseDate is null");
	        return simpledateformat.format(new Date());
	    }
		return simpledateformat.format(this.submissionReleaseDate);
	}

    
    /**
     * Returns the submission update date in a yyy/MM/dd format, or 
     * the current date if no update date is specified. 
     * 
     *  @return String
     */
	public String getSubmissionUpdateDateAsString(){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
		if (this.submissionUpdateDate == null){
		    return this.getSubmissionReleaseDateAsString();
		}
		return simpledateformat.format(this.submissionUpdateDate);
	}
	
	/**
	 * Either adds the provided TermSource and returns its name 
	 * or returns the name of an existing TermSource with the same URI.
	 * 
     *  @return String
	 */
	public String getOrAddTermSource(TermSource termSource){
	    for (TermSource ts : termSources){
	        if ((ts.getURI() == null && termSource.getURI() == null) 
	                || (ts.getURI()!= null && ts.getURI().equals(termSource.getURI()))){
	            return ts.getName();
	        }
	    }
	    //termSource is not already in MSI, add it
	    if (!termSources.contains(termSource)){
	        termSources.add(termSource);
	    }
	    return termSource.getName();
	}
    
    /**
     * Convinently lookup a term source by its name
     * 
     *  @return TermSource
     */
    public TermSource getTermSource(String name){
        for (TermSource ts : termSources){
            if (ts.getName() != null && ts.getName().equals(name)){
                return ts;
            }
        }
        return null;
    }
	
	
}
