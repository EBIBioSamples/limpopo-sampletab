package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.comparator.ComparatorSortedSet;
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
public class MSI implements Comparable<MSI> {
	
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

	//for internal use in error logs
	private URL location = null;
    
	private Logger log = LoggerFactory.getLogger(getClass());

    public void setLocation(URL location) {
        if (this.location != null ) {
            throw new IllegalArgumentException("Can only specify location once");
        }
        this.location = location;
    }
    
    public URL getLocation() {
        return location;
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
     * Conveniently lookup a term source by its name
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
    
    /**
     * Conveniently lookup PubMed IDs
     * 
     *  @return Collection<Integer>
     */
    public Collection<Integer> getPubmedIDs() {
        Collection<Integer> pubmedids = new ArrayList<Integer>();
        for (Publication p : publications) {
            Integer pubmedid = null;
            try {
                pubmedid = Integer.parseInt(p.getPubMedID());
            } catch (NumberFormatException e) {
                //do nothing
            }
            if (pubmedid != null && !pubmedids.contains(pubmedid)){
                pubmedids.add(pubmedid);
            }
        }
        return pubmedids;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (!MSI.class.isInstance(other)){
            return false;
        } else {
            MSI mother = (MSI) other;

            //convert to sorted sets for comparisons
            
            SortedSet<Organization> sso1 = new TreeSet<Organization>();
            sso1.addAll(this.organizations);
            SortedSet<Organization> sso2 = new TreeSet<Organization>();
            sso2.addAll(mother.organizations);
            
            SortedSet<Person> ssp1 = new TreeSet<Person>();
            ssp1.addAll(this.persons);
            SortedSet<Person> ssp2 = new TreeSet<Person>();
            ssp2.addAll(mother.persons);

            SortedSet<Publication> ssu1 = new TreeSet<Publication>();
            ssu1.addAll(this.publications);
            SortedSet<Publication> ssu2 = new TreeSet<Publication>();
            ssu2.addAll(mother.publications);
            
            SortedSet<TermSource> sst1 = new TreeSet<TermSource>();
            sst1.addAll(this.termSources);
            SortedSet<TermSource> sst2 = new TreeSet<TermSource>();
            sst2.addAll(mother.termSources);
            
            SortedSet<Database> ssd1 = new TreeSet<Database>();
            ssd1.addAll(this.databases);
            SortedSet<Database> ssd2 = new TreeSet<Database>();
            ssd2.addAll(mother.databases);
            
            return new EqualsBuilder()
            .append(this.submissionIdentifier, mother.submissionIdentifier)
            .append(this.submissionTitle, mother.submissionTitle)
            .append(this.submissionReferenceLayer, mother.submissionReferenceLayer)
            .append(this.submissionDescription, mother.submissionDescription)
            .append(this.submissionReleaseDate, mother.submissionReleaseDate)
            .append(this.submissionUpdateDate, mother.submissionUpdateDate)
            .append(this.submissionVersion, mother.submissionVersion)
            .append(sso1, sso2)
            .append(ssp1, ssp2)
            .append(ssu1, ssu2)
            .append(sst1, sst2)
            .append(ssd1, ssd2)
                .isEquals();
        }
    }

    public int hashCode() {
        //convert to sorted sets for comparisons
        
        SortedSet<Organization> sso = new TreeSet<Organization>();
        sso.addAll(this.organizations);
        
        SortedSet<Person> ssp = new TreeSet<Person>();
        ssp.addAll(this.persons);

        SortedSet<Publication> ssu = new TreeSet<Publication>();
        ssu.addAll(this.publications);
        
        SortedSet<TermSource> sst = new TreeSet<TermSource>();
        sst.addAll(this.termSources);
        
        SortedSet<Database> ssd = new TreeSet<Database>();
        ssd.addAll(this.databases);
        
        return new HashCodeBuilder(7, 31) // two randomly chosen prime numbers
        .append(this.submissionIdentifier)
        .append(this.submissionTitle)
        .append(this.submissionReferenceLayer)
        .append(this.submissionDescription)
        .append(this.submissionReleaseDate)
        .append(this.submissionUpdateDate)
        .append(this.submissionVersion)
        .append(sso)
        .append(ssp)
        .append(ssu)
        .append(sst)
        .append(ssd)
            .toHashCode();
    }

    public int compareTo(MSI other) {
        if (other == null) {
            return -1;
        } else if (this.equals(other)){
            return 0;
        } else {

            //convert to sorted sets for comparisons
            
            SortedSet<Organization> sso1 = new TreeSet<Organization>();
            sso1.addAll(this.organizations);
            SortedSet<Organization> sso2 = new TreeSet<Organization>();
            sso2.addAll(other.organizations);
            ComparatorSortedSet<Organization> co = new ComparatorSortedSet<Organization>();
            
            SortedSet<Person> ssp1 = new TreeSet<Person>();
            ssp1.addAll(this.persons);
            SortedSet<Person> ssp2 = new TreeSet<Person>();
            ssp2.addAll(other.persons);
            ComparatorSortedSet<Person> cp = new ComparatorSortedSet<Person>();

            SortedSet<Publication> ssu1 = new TreeSet<Publication>();
            ssu1.addAll(this.publications);
            SortedSet<Publication> ssu2 = new TreeSet<Publication>();
            ssu2.addAll(other.publications);
            ComparatorSortedSet<Publication> cu = new ComparatorSortedSet<Publication>();
            
            SortedSet<TermSource> sst1 = new TreeSet<TermSource>();
            sst1.addAll(this.termSources);
            SortedSet<TermSource> sst2 = new TreeSet<TermSource>();
            sst2.addAll(other.termSources);
            ComparatorSortedSet<TermSource> ct = new ComparatorSortedSet<TermSource>();
            
            SortedSet<Database> ssd1 = new TreeSet<Database>();
            ssd1.addAll(this.databases);
            SortedSet<Database> ssd2 = new TreeSet<Database>();
            ssd2.addAll(other.databases);
            ComparatorSortedSet<Database> cd = new ComparatorSortedSet<Database>();
            
            
            return new CompareToBuilder()
                .append(this.submissionIdentifier, other.submissionIdentifier)
                .append(this.submissionTitle, other.submissionTitle)
                .append(this.submissionReferenceLayer, other.submissionReferenceLayer)
                .append(this.submissionDescription, other.submissionDescription)
                .append(this.submissionReleaseDate, other.submissionReleaseDate)
                .append(this.submissionUpdateDate, other.submissionUpdateDate)
                .append(this.submissionVersion, other.submissionVersion)
                .append(sso1, sso2, co)
                .append(ssp1, ssp2, cp)
                .append(ssu1, ssu2, cu)
                .append(sst1, sst2, ct)
                .append(ssd1, ssd2, cd)
                .toComparison();
        }
    }
	
	
}
