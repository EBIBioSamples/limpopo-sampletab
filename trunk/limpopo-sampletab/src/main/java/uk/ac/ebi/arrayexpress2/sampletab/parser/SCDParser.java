package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.HandlerLoader;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressEvent;
import uk.ac.ebi.arrayexpress2.magetab.parser.AbstractParser;
import uk.ac.ebi.arrayexpress2.magetab.utils.MAGETABUtils;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.SCDReadHandler;

public class SCDParser extends AbstractParser<SCD> {
    public SCD parse(InputStream scdIn) throws ParseException {
        return parse(scdIn, new SCD());
    }

    public SCD parse(InputStream scdIn, SCD scd) throws ParseException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        //ExecutorService service = Executors.newCachedThreadPool();
        SCD result = parse(scdIn, scd, service);
        try {
            service.shutdown();
            service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            return result;
        }
        catch (InterruptedException e) {
            throw new ParseException("Parsing was interrupted", e);
        }
    }
    
    public SCD parse(InputStream scdIn, SCD scd, ExecutorService service) throws ParseException {
        getLog().debug("Starting SCD parsing...");
        fireParsingStartedEvent(new ProgressEvent());

        try {
            getLog().debug("Tabulating SCD data...");
            String[][] scdData = MAGETABUtils.readTabDelimitedInputStream(scdIn,
                                                                           DEFAULT_ENCODING,
                                                                           isStrippingEscaping(),
                                                                           false);
            getLog().debug("Tabulated " + scdData.length + " lines of SCD data");
            read(scdData, scd, service);

            // and return the resulting SCD
            return scd;
        }
        catch (IOException e) {
            String message = "The SCD file could not be read (" + e.getMessage() + ")";
            getLog().error(message, e);
            fireParsingFailedEvent(new ProgressEvent());
            throw new ParseException(message, e);
        }
        catch (InterruptedException e) {
            String message = "The SCD parsing operation was interrupted (" + e.getMessage() + ")";
            getLog().error(message, e);
            fireParsingFailedEvent(new ProgressEvent());
            throw new ParseException(message, e);
        }
        catch (ExecutionException e) {
            String message = "Failed to execute an SCD parse operation (" + e.getMessage() + ")";
            getLog().error(message, e);
            fireParsingFailedEvent(new ProgressEvent());
            throw new ParseException(message, e);
        }
    }

    /**
     * Reads tabulated SCD data into the supplied SCD object, delegating the execution of handling tasks to the
     * supplied ExecutorService.  Executes asynchronously.
     *
     * @param scdData the SCD data to handle
     * @param scd     the SCD into which parsed data should be added
     * @param service  the service to execute handlers within
     * @throws ExecutionException   if any handling tasks failed to execute
     * @throws InterruptedException if the service was interrupted whilst parsing was ongoing
     */
    protected void read(String[][] scdData, final SCD scd, ExecutorService service)
            throws ExecutionException, InterruptedException {
        // create a listener that will track progress of all handlers for this SCD
        DefaultHandlerListener listener = new DefaultHandlerListener();

        // now, create a handler for each block of data
        getLog().debug("Handling data...");

        // create handler tasks for each line, store them temporarily in a deque to be queued for execution
        final BlockingDeque<Callable<Void>> taskDeque = new LinkedBlockingDeque<Callable<Void>>();

        if (scdData.length == 0){
            String message = "No SCD headers found.";
            getLog().error(message);
            fireParsingFailedEvent(new ProgressEvent());
            return;
        }
        
        // retain header (first row), needs to go to each handler
        final String[] header = MAGETABUtils.digestHeaders(scdData[0]);

        // collection of columns we know we can't read
        Collection<String> unreadableColumns = new HashSet<String>();

        // iterate over data contents
        if (scdData.length > 0){
	        for (int lineNumber = 1; lineNumber < scdData.length; lineNumber++) {
	            String[] data = scdData[lineNumber];
	            // only parse non-empty, non-comment lines
	            if (data.length > 0 && !data[0].startsWith("#")) {
	                // create the task that generates graph handler tasks and queue it
	                createReadHandlerTasks(scd,
	                                       header,
	                                       data,
	                                       lineNumber,
	                                       unreadableColumns,
	                                       taskDeque,
	                                       listener);
	            }
	        }
        }
        // submit all tasks in deque, block while tasks are still being submitted
        int taskCount = taskDeque.size();
        while (taskDeque.peek() != null) {
            // take the first task and execute it
            Callable<Void> nextTask = taskDeque.take();
            service.submit(nextTask);
        }

        // and notify the listener that we've submitted all the tasks we need
        listener.finalizeHandlers();
        getLog().debug("Submitted " + taskCount + " SCD reading tasks");
    }

    /**
     * Creates, and adds to the supplied deque, a callable that runs a unit of work to generate handler tasks to read
     * from the SCD.
     *
     * @param scd              the SCD to read into
     * @param header            the header of this part of the SCD
     * @param data              the line data for this part of the SCD
     * @param lineNumber        the current line number
     * @param unreadableColumns tracks the columns we know we can't read, so as not to warn repeatedly
     * @param taskDeque         the deque to queue tasks into
     * @param listener          the listener to use to track the progress of each handler
     */
    private void createReadHandlerTasks(final SCD scd,
                                        final String[] headerIn,
                                        final String[] dataIn,
                                        final int lineNumber,
                                        final Collection<String> unreadableColumns,
                                        final Deque<Callable<Void>> taskDeque,
                                        final DefaultHandlerListener listener) {

        getLog().trace("Creating handlers for " + headerIn.toString());

        // start from 0-index column
        int columnNumber = 0;

        // find any handlers for this line data
        while (columnNumber < headerIn.length) {
            // get the next part of the header
            final String[] headerPart = MAGETABUtils.extractRange(headerIn, columnNumber);
            // get the next part of the data
            final String[] dataPart = MAGETABUtils.extractRange(dataIn, columnNumber);

            // get the handler(s) for this part of the graph
            Collection<SCDReadHandler> handlers = HandlerLoader.getHandlerLoader().getReadHandlersOfType(SCDReadHandler.class, headerPart);

            // create a callable for each handler
            if (handlers.isEmpty()) {
                // no handlers for this column, or else only 1 handler that signifies unknown header
                if (!unreadableColumns.contains(headerPart[0])) {
                    // no handlers for this column, so skip it
                    String message = "Column " + columnNumber + " (" + headerPart[0] + ") cannot be read and " +
                            "will be skipped - no handler can read data from this column in it's current location.  " +
                            "Skipping.";

                    if (headerPart[0].trim().length() > 0){
                        // log a warning - we'll ignore this tag
                        // only log the warning if its not a blank column
                        getLog().warn(message);
                    }

                    unreadableColumns.add(headerPart[0]);
                }
                columnNumber++;
            }
            else {
                int readsTo = -1;
                for (final SCDReadHandler handler : handlers) {
                    // the column we'll read to (from handler.assess())
                    int handlerReadsTo;
                    // how far ahead can this handler read?
                    final int readsFrom = columnNumber;
                    handlerReadsTo = handler.assess(headerPart);

                    // notify the listener of the next handler
                    listener.addHandler(handler, dataPart);

                    // add the listener to this handler if we've not already done so
                    if (!handler.getListeners().contains(listener)) {
                        handler.addListener(listener);
                    }
                    // add a task to handle this part of the graph
                    taskDeque.add(new Callable<Void>() {
                        public Void call() throws Exception {
                            executeReadHandler(scd,
                        		headerPart,
                        		dataPart,
                        		lineNumber,
                        		readsFrom,
                        		handler);
                            return null;
                        }
                    });

                    // update readTo if this handler can read further that any others
                    if (handlerReadsTo > readsTo) {
                        readsTo = handlerReadsTo;
                    }
                }
                //System.out.println(headerPart[0]);
                //System.out.println(readsTo);

                // update column number
                columnNumber += readsTo;
            }
        }
    }

    private void executeReadHandler(SCD scd,
                                    String[] header,
                                    String[] data,
                                    int lineNumber,
                                    int columnNumber,
                                    SCDReadHandler scdReadHandler) {
        try {
            // read the data with the given handler
            scdReadHandler.read(header, data, scd, lineNumber, columnNumber);
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
                // set the line number
                item.setLine(lineNumber);
                // add column number to the relative column number (which will already be set)
                item.setCol(item.getCol() == -1 ? columnNumber : item.getCol() + columnNumber);
                // and fire the event
                fireErrorItemEvent(item);
            }
            fireParsingFailedEvent(new ProgressEvent());
        }
        catch (Exception e) {
            e.printStackTrace();
            fireParsingFailedEvent(new ProgressEvent());
        }
        finally {
            fireParseEvent(new ProgressEvent());
        }
    }
}
