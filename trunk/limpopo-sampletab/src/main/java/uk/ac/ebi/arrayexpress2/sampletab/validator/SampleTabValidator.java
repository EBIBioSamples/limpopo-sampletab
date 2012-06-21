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
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Publication;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.TermSource;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttributeOntology;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class SampleTabValidator extends AbstractValidator<SampleData> {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    private ErrorItemFactory errorFactory = ErrorItemFactory.getErrorItemFactory(); 
    
    public ErrorItem getErrorItemFromCode(int errorCode){
        return getErrorItemFromCode("", errorCode);
    }
    
    public ErrorItem getErrorItemFromCode(String errorMessage, int errorCode){
        ErrorItem error = errorFactory.generateErrorItem(errorMessage, errorCode, this.getClass());
        return error;
    }
    
	public void validate(SampleData sampledata) throws ValidateException {        
        List<ErrorItem> errors = new ArrayList<ErrorItem>();

        Date now = new Date();

        //Submission Update Date must be in the past
        if (sampledata.msi.submissionUpdateDate.after(now)){
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
