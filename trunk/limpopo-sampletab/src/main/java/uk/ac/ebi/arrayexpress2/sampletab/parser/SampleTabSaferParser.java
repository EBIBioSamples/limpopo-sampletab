package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mged.magetab.error.ErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.magetab.validator.Validator;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class SampleTabSaferParser extends SampleTabParser<SampleData> {
    private final List<ErrorItem> errorItems = new ArrayList<ErrorItem>();

    public Logger log = LoggerFactory.getLogger(getClass());

    public SampleTabSaferParser() {
        this(null);
    }
	
	public SampleTabSaferParser(Validator<SampleData> validator) {
        super(validator);
        //make a listener to put error items in the list
        super.addErrorItemListener(new ErrorItemListener() {
            public void errorOccurred(ErrorItem item) {
                errorItems.add(item);
            }
        });
    }

    public synchronized SampleData parse(InputStream dataIn, SampleData target)
			throws ParseException {      
        

        SampleData sd = null;
        ExecutorService service = Executors.newSingleThreadExecutor();
        
        try { 
            sd = super.parse(dataIn, target, service);
            if (errorItems.size() > 0){
                throw new ParseException(true, errorItems.size()+" error items detected", errorItems.toArray(new ErrorItem[]{}));
            }
        }
        catch (InterruptedException e) {
            throw new ParseException("Parsing was interrupted", e);
        }
        finally {
            service.shutdown();
            try {
                service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            }
            catch (InterruptedException e) {
                // take drastic measures to terminate
                log.warn("Service shutdown was interrupted, forcing termination");
                service.shutdownNow();
            }
        }
        return sd;
	}
}
