package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.converter.Converter;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressEvent;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressListener;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressListenerAdapter;
import uk.ac.ebi.arrayexpress2.magetab.parser.AbstractParser;
import uk.ac.ebi.arrayexpress2.magetab.validator.Validator;
import uk.ac.ebi.arrayexpress2.sampletab.converter.SampleTabConverter;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;
import uk.ac.ebi.arrayexpress2.sampletab.validator.SampleTabValidator;

public class SampleTabParser<O> extends AbstractParser<SampleData> {
    private final Validator<SampleData> validator;
    private final Converter<SampleData, O> converter;
    private final O[] outputResources;

    private final MSIParser msiParser;
    private final SCDParser scdParser;

    public SampleTabParser() {
        this(new SampleTabValidator());
    }
    
    public SampleTabParser(O... outputResources) {
        this(new SampleTabValidator(), new SampleTabConverter<O>(), outputResources);
    }
    
    public SampleTabParser(Validator<SampleData> validator) {
        this(validator, new SampleTabConverter<O>());
    }
    
    public SampleTabParser(Converter<SampleData, O> converter, O... outputResources) {
        this(new SampleTabValidator(), converter, outputResources);
    }
    
    public SampleTabParser(Validator<SampleData> validator,
                         Converter<SampleData, O> converter,
                         O... outputResources) {
        super();

        msiParser = new MSIParser();
        scdParser = new SCDParser();
        
        this.validator = validator;
        this.converter = converter;
        this.outputResources = outputResources;
    }

    public Validator<SampleData> getValidator() {
        return validator;
    }

    public Converter<SampleData, O> getConverter() {
        return converter;
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
        getLog().info("Starting parsing "+filename+"...");
    	return parse(new File(filename));
    }

	public SampleData parse(InputStream dataIn) throws ParseException {
        return parse(dataIn, new SampleData());
	}
	
	public SampleData parse(InputStream dataIn, SampleData target)
			throws ParseException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
        	SampleData result = parse(dataIn, target, service);
            service.shutdown();
            service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            return result;
        }
        catch (InterruptedException e) {
            throw new ParseException("Parsing was interrupted", e);
        }
	}

    @Override
    public void addErrorItemListener(ErrorItemListener listener) {
        super.addErrorItemListener(listener);
        msiParser.addErrorItemListener(listener);
        scdParser.addErrorItemListener(listener);

        // also register to the validator, if present
        if (getValidator() != null) {
            getValidator().addErrorItemListener(listener);
        }

        // also register to the converter, if present
        if (getConverter() != null) {
            getConverter().addErrorItemListener(listener);
        }
    }

    @Override
    public void removeErrorItemListener(ErrorItemListener listener) {
        super.removeErrorItemListener(listener);
        msiParser.removeErrorItemListener(listener);
        scdParser.removeErrorItemListener(listener);

        // also remove from the validator, if present
        if (getValidator() != null) {
            getConverter().removeErrorItemListener(listener);
        }

        // also remove from the converter, if present
        if (getConverter() != null) {
            getConverter().removeErrorItemListener(listener);
        }
    }

	public SampleData parse(InputStream dataIn, SampleData target,
			ExecutorService service) throws ParseException,
			InterruptedException {
		
        fireParsingStartedEvent(new ProgressEvent());
        getLog().debug("Commencing parse operation.");

        // create a flag that we can update once the scd starts
        final ProgressFlag flag = new ProgressFlag();

        // create an msi progress listener, and register it to the MSI
        ProgressListener msiListener = new MSIProgressListener(flag, target, service);
        msiParser.addProgressListener(msiListener);

        // create an scd progress listener, and register it to the SCD
        ProgressListener scdListener = new SCDProgressListener(flag);
        scdParser.addProgressListener(scdListener);

        // trigger MSI read
        // TODO replace this with true stream splitting, but need to make sure the parser doesnt close the underlying streams first 
        StringBuffer msiBuffer = new StringBuffer();
        StringBuffer scdBuffer = new StringBuffer();
        StringBuffer buffer = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(dataIn));
        
        try {
        	String line;
	        while ((line = in.readLine()) != null) {
	        		if (line.contains("[MSI]")){
	        			buffer = msiBuffer;
	        		} else if (line.contains("SCD")){
	        			buffer = scdBuffer;
	        		} else if (buffer != null){
	        			buffer.append(line);
	        			buffer.append("\n");
	        		}
	        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String msiString = msiBuffer.toString();
        InputStream msiInput = new StringBufferInputStream(msiString);
        readMSI(msiInput, target.msi, service, flag);
        
        String scdString = scdBuffer.toString();
        InputStream scdInput = new StringBufferInputStream(scdString);
        readSCD(scdInput, target.scd, service, flag);
        

        // we must not return until all tasks have been added -
        // we know this is the case once MSI parsing and SCD parsing have started
        synchronized (flag) {
            getLog().debug("Waiting until all MSI and SCD tasks have been submitted...");
            while (!(flag.hasMSITasksAdded() && flag.hasSCDTasksAdded())) {
                getLog().debug("Waiting until all " + (flag.hasMSITasksAdded() ? "MSI and SCD" : "SCD") +
                                       " tasks have been submitted...");
                flag.wait();
            }
            getLog().debug("...done!");
        }

        // are we validating or writing? if so, we have to wait until parsing is complete
        if (getValidator() != null || getConverter() != null) {
            // we must not validate or convert until all parsing tasks have finished
            synchronized (flag) {
                getLog().debug("Waiting until all MSI and SCD tasks have finished...");
                while (!(flag.hasMSIFinished() && flag.hasSCDFinished())) {
                    getLog().debug("Waiting until all " + (flag.hasMSIFinished() ? "MSI and SCD" : "SCD") +
                                           " parsing tasks have finsihed...");
                    flag.wait();
                }
                getLog().debug("...done!");
            }

            // trigger validate
            if (getValidator() != null) {
                getLog().info("Doing validation with " + getValidator().getClass().getSimpleName());
                getValidator().validate(target, service);
            }

            // trigger convert
            if (getConverter() != null) {
                for (O outputResource : outputResources) {
                    getLog().info("Doing conversion to " + outputResource.getClass().getSimpleName() +
                                          " with " + getConverter().getClass().getSimpleName());
                    getConverter().convert(target, outputResource, service);
                }
            }
        }

        // now we've listened for the events we care about, remove listeners
        msiParser.removeProgressListener(msiListener);
        scdParser.removeProgressListener(scdListener);

        // and return a reference to the sample data being parsed
        return target;
	}
	

    protected void readMSI(final InputStream msiIn,
                           final MSI msi,
                           final ExecutorService service,
                           final ProgressFlag flag) {
        // start IDF parsing
        service.submit(new Callable<MSI>() {
            public MSI call() throws ParseException {
                try {
                    return msiParser.parse(msiIn, msi, service);
                }
                catch (ParseException e) {
                    // get error items from the exception
                    for (ErrorItem item : e.getErrorItems()) {
                        // set type
                        if (e.isCriticalException()) {
                            item.setErrorType("parse warning");
                        }
                        else {
                            item.setErrorType("parse error");
                        }
                        // and fire the event
                        fireErrorItemEvent(item);
                    }
                    fireParsingFailedEvent(new ProgressEvent());
                    throw e;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    fireParsingFailedEvent(new ProgressEvent());
                    throw new ParseException(e);
                }
                finally {
                    // we know by contract that all IDF tasks must have been submitted to our service
                    flag.setMSITasksAdded(true);
                }
            }
        });
    }

    protected void readSCD(final InputStream scdIn,
                            final SCD scd,
                            final ExecutorService service,
                            final ProgressFlag flag) {
        // start SDRF parsing
        service.submit(new Callable<SCD>() {
            public SCD call() throws ParseException {
                try {
                    return scdParser.parse(scdIn, scd, service);
                }
                catch (ParseException e) {
                    // get error items from the exception
                    for (ErrorItem item : e.getErrorItems()) {
                        // set type
                        if (e.isCriticalException()) {
                            item.setErrorType("parse warning");
                        }
                        else {
                            item.setErrorType("parse error");
                        }
                        // and fire the event
                        fireErrorItemEvent(item);
                    }
                    fireParsingFailedEvent(new ProgressEvent());
                    throw e;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    fireParsingFailedEvent(new ProgressEvent());
                    throw new ParseException(e);
                }
                finally {
                    // we know by contract that all SDRF tasks must have been submitted to our service
                    flag.setSCDTasksAdded(true);
                }
            }
        });
    }	

    private class MSIProgressListener extends ProgressListenerAdapter {
        private final ProgressFlag flag;
        private final SampleData data;
        private final ExecutorService service;

        public MSIProgressListener(ProgressFlag flag, SampleData data, ExecutorService service) {
            this.flag = flag;
            this.data = data;
            this.service = service;
        }

        public void parsingStarted(ProgressEvent evt) {
            flag.setMSIStarted(true);
        }

        public void parsingCompleted(ProgressEvent evt) {
            getLog().info("Parsed MSI successfully");
            flag.setMSITasksAdded(true);
            flag.setMSIFinished(true);

            // fire a completion event on this parser if all done
            if (flag.hasMSIFinished() && flag.hasSCDFinished()) {
                fireParsingCompletedEvent(evt);
            }
        }

        public void parsingFailed(ProgressEvent evt) {
            // cascade event up to this parsers progress listener
            fireParsingFailedEvent(evt);
            getLog().error("Failed to parse MSI");
            flag.setMSITasksAdded(true);
            flag.setMSIFinished(true);
        }
    }

    private class SCDProgressListener extends ProgressListenerAdapter {
        private final ProgressFlag flag;

        public SCDProgressListener(ProgressFlag flag) {
            this.flag = flag;
        }

        public void parsingStarted(ProgressEvent evt) {
            flag.setSCDStarted(true);
        }

        public void parsingCompleted(ProgressEvent evt) {
            getLog().info("Parsed SCD successfully");
            flag.setSCDFinished(true);

            // fire a completion event on this parser if all done
            if (flag.hasMSIFinished() && flag.hasSCDFinished()) {
                fireParsingCompletedEvent(evt);
            }
        }

        public void parsingFailed(ProgressEvent evt) {
            // cascade event up to this parsers progress listener
            fireParsingFailedEvent(evt);

            getLog().error("Failed to parse SCD");
            flag.setSCDFinished(true);
        }

        public void parsedMore(ProgressEvent evt) {
            // cascade event up to this parsers progress listener
            fireParseEvent(evt);

            getLog().trace("Parsed a bit more SCD");
        }
    }

    private class ProgressFlag {
        private boolean msiTasksAdded = false;
        private boolean msiStarted = false;
        private boolean msiFinished = false;

        private boolean scdTasksAdded = false;
        private boolean scdStarted = false;
        private boolean scdFinished = false;

        public synchronized boolean hasMSITasksAdded() {
            return msiTasksAdded;
        }

        public synchronized void setMSITasksAdded(boolean msiTasksAdded) {
            getLog().debug("MSI tasks added");
            this.msiTasksAdded = msiTasksAdded;
            this.notifyAll();
        }

        public synchronized boolean hasMSIStarted() {
            return msiStarted;
        }

        public synchronized void setMSIStarted(boolean msiStarted) {
            getLog().debug("MSI parsing started");
            this.msiStarted = msiStarted;
            this.notifyAll();
        }

        public synchronized boolean hasMSIFinished() {
            return msiFinished;
        }

        public synchronized void setMSIFinished(boolean msiFinished) {
            if (msiFinished) {
                getLog().debug("MSI parsing finished");
                this.msiTasksAdded = true;
            }
            this.msiFinished = msiFinished;
            this.notifyAll();
        }

        public synchronized boolean hasSCDTasksAdded() {
            return scdTasksAdded;
        }

        public synchronized void setSCDTasksAdded(boolean scdTasksAdded) {
            getLog().debug("SCD tasks added");
            this.scdTasksAdded = scdTasksAdded;
            this.notifyAll();
        }

        public synchronized boolean hasSCDStarted() {
            return scdStarted;
        }

        public synchronized void setSCDStarted(boolean scdStarted) {
            getLog().debug("SCD parsing started");
            this.scdStarted = scdStarted;
            this.notifyAll();
        }

        public synchronized boolean hasSCDFinished() {
            return scdFinished;
        }

        public synchronized void setSCDFinished(boolean scdFinished) {
            if (scdFinished) {
                getLog().debug("SCD parsing finished");
                this.scdTasksAdded = true;
            }
            this.scdFinished = scdFinished;
            this.notifyAll();
        }
    }
}
