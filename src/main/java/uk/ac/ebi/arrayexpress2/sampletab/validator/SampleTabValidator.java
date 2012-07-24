package uk.ac.ebi.arrayexpress2.sampletab.validator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ValidateException;
import uk.ac.ebi.arrayexpress2.magetab.validator.AbstractValidator;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Database;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Organization;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Publication;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.TermSource;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttributeOntology;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.DatabaseAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class SampleTabValidator extends AbstractValidator<SampleData> {
    

    protected final List<ErrorItem> errors = new ArrayList<ErrorItem>();
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    private ErrorItemFactory errorFactory = ErrorItemFactory.getErrorItemFactory(); 
    
    protected ErrorItem getErrorItemFromCode(int errorCode){
        return getErrorItemFromCode("", errorCode);
    }
    
    protected ErrorItem getErrorItemFromCode(String errorMessage, int errorCode){
        ErrorItem error = errorFactory.generateErrorItem(errorMessage, errorCode, this.getClass());
        return error;
    }
    
	public synchronized void validate(SampleData sampledata) throws ValidateException {      

        Date now = new Date();

        //Submission Update Date must be in the past
        if (sampledata.msi.submissionUpdateDate != null && sampledata.msi.submissionUpdateDate.after(now)){
            errors.add(getErrorItemFromCode(1529));
        }
        
        //Submission Release Date must be specified
        if (sampledata.msi.submissionReleaseDate == null){
            errors.add(getErrorItemFromCode(1534));
        }
        
        
        //Term Source checks
        //get the names used in SCD
        Set<String> usedTsNames = new HashSet<String>();
        for (SCDNode scdnode : sampledata.scd.getAllNodes()){
            for (SCDNodeAttribute attr : scdnode.getAttributes()){
                if (AbstractNodeAttributeOntology.class.isInstance(attr)){
                    AbstractNodeAttributeOntology attrOnt = (AbstractNodeAttributeOntology) attr;
                    //if this attribute has a term source at all
                    if (attrOnt.getTermSourceREF() != null && attrOnt.getTermSourceREF().trim().length() > 0){
                        //add it to the pool
                        usedTsNames.add(attrOnt.getTermSourceREF().trim());
                    }
                }
            }
        }
        
        //get the names defined in MSI
        Set<String> tsNames = new HashSet<String>();
        Set<URI> tsURIs = new HashSet<URI>();
        for (TermSource ts : sampledata.msi.termSources){
            //all should be unique
            if (tsNames.contains(ts.getName())){
                errors.add(getErrorItemFromCode(ts.getName(), 1532));
            }
            //add the name to the pile of seen names
            tsNames.add(ts.getName());
            
            //all should have unique & valid URIs
            if (ts.getURI() == null){ 
                errors.add(getErrorItemFromCode(ts.getURI(), 1533));
            } else {
                URI tsURI = null;
                try { 
                    tsURI = new URI(ts.getURI());
                } catch (URISyntaxException e) {
                    //invalid format URI
                    errors.add(getErrorItemFromCode(ts.getURI(), 1533));
                }
                if (tsURI != null){
                    if (tsURIs.contains(tsURI)){
                        errors.add(getErrorItemFromCode(ts.getURI(), 1535));
                    }
                    tsURIs.add(tsURI);
                }
            }
        }
        
        //check used names are defined
        for (String usedTsName : usedTsNames){
            if (!tsNames.contains(usedTsName)){
                errors.add(getErrorItemFromCode(usedTsName, 1530));
            }
        }
        //check defined names are used
        for (String tsName : tsNames){
            if (!usedTsNames.contains(tsName)){
                errors.add(getErrorItemFromCode(tsName, 1531));
            }
        }
        
        
        //PubMed IDs must be valid
        for (Publication publication : sampledata.msi.publications){
            publication.getPubMedID();
        }


        //check attribute key and values are not null and at least 1 character long
        for (SCDNode scdnode : sampledata.scd.getAllNodes()){
            for (SCDNodeAttribute attr : scdnode.getAttributes()){
                if (attr.getAttributeType() == null || attr.getAttributeType().trim().length() == 0){
                    errors.add(getErrorItemFromCode(scdnode.getNodeName(), 1536));
                }
                if (attr.getAttributeValue() == null || attr.getAttributeValue().trim().length() == 0){
                    errors.add(getErrorItemFromCode(scdnode.getNodeName()+":"+attr.getAttributeType(), 1537));
                }
            }
        }
        
        for (SCDNode scdnode : sampledata.scd.getAllNodes()){
            for (SCDNodeAttribute attr : scdnode.getAttributes()){
                //check all term source ref have a term source id
                if (AbstractNodeAttributeOntology.class.isInstance(attr)){
                    AbstractNodeAttributeOntology attrOnt = (AbstractNodeAttributeOntology) attr;

                    if (attrOnt.getTermSourceREF() != null && attrOnt.getTermSourceREF().trim().length() > 0){
                        if (attrOnt.getTermSourceID() == null || attrOnt.getTermSourceID().trim().length() == 0){
                            errors.add(getErrorItemFromCode(attrOnt.getTermSourceREF()+" : "+attrOnt.getAttributeValue(), 1538));
                        }
                    }
                }
                //check all database URI references have a valid URI
                if (DatabaseAttribute.class.isInstance(attr)){
                    DatabaseAttribute attrDb = (DatabaseAttribute) attr;
                    if (attrDb.databaseURI == null){
                        //do nothing, or raise error?
                        //errors.add(getErrorItemFromCode(d.getURI(), 1540));
                    } else {
                        try {
                            new URI(attrDb.databaseURI);
                        } catch (URISyntaxException e) {
                            //invalid URI 
                            errors.add(getErrorItemFromCode(attrDb.databaseURI, 1540));
                        }
                    }
                }
            }
        }
        
        //check organization URIs are actually URIs
        for (Organization o : sampledata.msi.organizations){
            if (o.getURI() != null) {
                try {
                    new URI(o.getURI());
                } catch (URISyntaxException e) {
                    //invalid URI 
                    errors.add(getErrorItemFromCode(o.getURI(), 1539));
                }
            }
        }
        
        //check database URIs are actually URIs
        for (Database d : sampledata.msi.databases){
            if (d.getURI() == null){
                //do nothing, or raise error?
                //errors.add(getErrorItemFromCode(d.getURI(), 1540));
            } else {
                try {
                    new URI(d.getURI());
                } catch (URISyntaxException e) {
                    //invalid URI 
                    errors.add(getErrorItemFromCode(d.getURI(), 1540));
                }
            }
        }
        
        //some warnings...
        if (sampledata.msi.submissionTitle == null){
            log.warn("Submission Title is null");
        } else if (sampledata.msi.submissionTitle.length() < 10){
            log.warn("Submission Title is under 10 characters long");
        }

        if (sampledata.msi.submissionDescription == null){
            log.warn("Submission Description is null");
        } else if (sampledata.msi.submissionDescription.length() < 10){
            log.warn("Submission Description is under 10 characters long");
        }
        
      
        
        //if we have errors, throw an exception for them
        if (errors.size() > 0){
            //log errors for tracking
            for (ErrorItem error : errors){
                log.error(error.reportString());
            }
            ErrorItem[] errorsArray = new ErrorItem[errors.size()];
            errors.toArray(errorsArray);
            throw new ValidateException(false, errorsArray);
        }
       
	}

}
