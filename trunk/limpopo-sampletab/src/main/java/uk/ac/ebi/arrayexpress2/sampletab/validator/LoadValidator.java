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
        
        super.validate(sampledata);
        
        Date now = new Date();

        //release date is in the future
        if (sampledata.msi.submissionReleaseDate.after(now)){
            //errors.add(getErrorItemFromCode(1528));
            //dont throw this as an error, but warn instead
            log.warn("Release date is in the future "+sampledata.msi.getSubmissionReleaseDateAsString());
        }
        
        //TODO check all samples are in at least one group
    }
}
