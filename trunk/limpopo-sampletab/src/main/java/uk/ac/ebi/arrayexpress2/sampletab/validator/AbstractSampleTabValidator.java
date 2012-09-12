package uk.ac.ebi.arrayexpress2.sampletab.validator;

import java.util.ArrayList;
import java.util.List;
import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.validator.AbstractValidator;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public abstract class AbstractSampleTabValidator extends AbstractValidator<SampleData> {
    
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
    

}
