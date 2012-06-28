package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class SampleTabSaferParser extends SampleTabParser<SampleData> {
    private final SampleTabParser<SampleData> parser = new SampleTabParser<SampleData>();
    private final List<ErrorItem> errorItems = new ArrayList<ErrorItem>();

    public Logger log = LoggerFactory.getLogger(getClass());

    public SampleTabSaferParser() {
        //make a listener to put error items in the list
        parser.addErrorItemListener(new ErrorItemListener() {
            public void errorOccurred(ErrorItem item) {
                errorItems.add(item);
            }
        });
        
    }
    
    public SampleData parse(File msiFile) throws ParseException {
        try {
            return parse(msiFile.toURI().toURL());
        }
        catch (MalformedURLException e) {
            throw new ParseException("File '" + msiFile.getAbsolutePath() + " could not be resolved to a valid URL", e);
        }
    }

    public SampleData parse(URL msiURL) throws ParseException {
        try {
            return parse(msiURL.openStream());
        }
        catch (IOException e) {
            throw new ParseException("Could not open a connection to " + msiURL.toString(), e);
        }
    }
    
    public SampleData parse(String filename) throws ParseException {
        log.debug("Starting parsing "+filename+"...");
    	return parse(new File(filename));
    }

	public SampleData parse(InputStream dataIn) throws ParseException {
        return parse(dataIn, new SampleData());
	}
	
	public synchronized SampleData parse(InputStream dataIn, SampleData target)
			throws ParseException {      
        

        SampleData sd = null;
        ExecutorService service = Executors.newSingleThreadExecutor();
        
        try { 
            sd = parser.parse(dataIn, target, service);
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
