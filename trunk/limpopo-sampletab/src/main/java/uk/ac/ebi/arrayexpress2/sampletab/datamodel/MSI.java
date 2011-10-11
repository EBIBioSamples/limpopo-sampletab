package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSI {
    public volatile String submissionTitle = "";
    public volatile String submissionDescription = "";
    public volatile String submissionIdentifier = "";
    public volatile String submissionVersion = "1.0";
    public volatile String submissionReleaseDate = "";
    public volatile String submissionUpdateDate = "";
    public volatile String submissionReferenceLayer = "";
    
    public volatile List<String> publicationDOI = new ArrayList<String>();
    public volatile List<String> publicationPubMedID = new ArrayList<String>();
    
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
    
    public volatile List<String> termSourceName = new ArrayList<String>();
    public volatile List<String> termSourceURI = new ArrayList<String>();
    public volatile List<String> termSourceVersion = new ArrayList<String>();

    public volatile List<String> databaseName = new ArrayList<String>();
    public volatile List<String> databaseURI = new ArrayList<String>();
    public volatile List<String> databaseID = new ArrayList<String>();

    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }
}
