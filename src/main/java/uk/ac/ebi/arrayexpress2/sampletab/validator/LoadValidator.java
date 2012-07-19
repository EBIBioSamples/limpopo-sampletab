package uk.ac.ebi.arrayexpress2.sampletab.validator;

import java.util.Date;

import org.mged.magetab.error.ErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ValidateException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class LoadValidator extends SampleTabValidator {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    public synchronized void validate(SampleData sampledata) throws ValidateException {
        errors.clear();
        
        try {
            super.validate(sampledata);
        } catch (ValidateException e){
            errors.addAll(e.getErrorItems());
        }
        
        Date now = new Date();

        //release date is in the future
        if (sampledata.msi.submissionReleaseDate.after(now)){
            errors.add(getErrorItemFromCode(1528));
        }
        
        //TODO check all samples are in at least one group
        
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
