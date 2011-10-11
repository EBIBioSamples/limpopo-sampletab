package uk.ac.ebi.arrayexpress2.sampletab.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.HandlerLoader;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressEvent;
import uk.ac.ebi.arrayexpress2.magetab.parser.AbstractParser;
import uk.ac.ebi.arrayexpress2.magetab.utils.MAGETABUtils;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

public class MSIParser extends AbstractParser<MSI> {

    public MSI parse(InputStream msiIn) throws ParseException {
        return parse(msiIn, new MSI());
    }
    
    public MSI parse(InputStream msiIn, MSI msi) throws ParseException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        MSI result = parse(msiIn, msi, service);
        try {
            service.shutdown();
            service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            return result;
        }
        catch (InterruptedException e) {
            throw new ParseException("Parsing was interrupted", e);
        }
    }
    

    public MSI parse(InputStream msiIn, MSI msi, ExecutorService service) throws ParseException {
        getLog().info("Starting MSI parsing...");
        fireParsingStartedEvent(new ProgressEvent());

        try {
            getLog().debug("Tabulating MSI data...");
            String[][] msiData = MAGETABUtils.readTabDelimitedInputStream(msiIn,
                                                                          DEFAULT_ENCODING,
                                                                          isStrippingEscaping());
            getLog().debug("Tabulated " + msiData.length + " lines of MSI data");
            read(msiData, msi, service);

            // and return the resulting MSI
            return msi;
        }
        catch (IOException e) {
            String message = "The MSI file could not be read (" + e.getMessage() + ")";
            getLog().error(message, e);
            fireParsingFailedEvent(new ProgressEvent());
            throw new ParseException(message, e);
        }
        catch (InterruptedException e) {
            String message = "The MSI parsing operation was interrupted (" + e.getMessage() + ")";
            getLog().error(message, e);
            fireParsingFailedEvent(new ProgressEvent());
            throw new ParseException(message, e);
        }
        catch (ExecutionException e) {
            String message = "Failed to execute an MSI parse operation (" + e.getMessage() + ")";
            getLog().error(message, e);
            fireParsingFailedEvent(new ProgressEvent());
            throw new ParseException(message, e);
        }
    }
    

    protected void read(String[][] msiData, MSI msi, ExecutorService service)
            throws ExecutionException, InterruptedException {
        // create a listener that will track progress of all handlers for this IDF
        DefaultHandlerListener listener = new DefaultHandlerListener();

        // now, create handler tasks for each line
        getLog().debug("Handling data...");
        int taskCount = 0;
        for (int lineNumber = 0; lineNumber < msiData.length; lineNumber++) {
            String[] nextLine = MAGETABUtils.removeTrailingEmptyCells(msiData[lineNumber]);

            // only parse non-empty, non-comment lines
            if (nextLine.length > 0 && !nextLine[0].startsWith("#")) {
                for (Callable<Void> nextTask : createReadHandlerTasks(msi, nextLine, lineNumber, listener)) {
                    taskCount++;
                    service.submit(nextTask);
                }
            }
        }

        // notify the listener that we've submitted all the tasks we need
        listener.finalizeHandlers();
        getLog().debug("Submitted " + taskCount + " MSI reading tasks");
    }

    private Collection<Callable<Void>> createReadHandlerTasks(MSI msi,
                                                              String[] lineData,
                                                              int lineNumber,
                                                              DefaultHandlerListener listener) {
        getLog().trace("Creating handlers for " + lineData[0]);

        // extract the header for the lineData
        String header = MAGETABUtils.digestHeader(lineData[0]);
        String[] data = MAGETABUtils.extractRange(lineData, 1);

        // find any handlers for this line data
        Collection<MSIReadHandler> handlers = HandlerLoader.getHandlerLoader().getReadHandlersOfType(MSIReadHandler.class, header);

        // create a callable for each handler
        Collection<Callable<Void>> lineHandlerTasks = new HashSet<Callable<Void>>();
        for (MSIReadHandler msiReadHandler : handlers) {
            // notify the listener of the next handler
            listener.addHandler(msiReadHandler, data);

            // add the listener to this handler if we've not already done so
            if (!msiReadHandler.getListeners().contains(listener)) {
            	msiReadHandler.addListener(listener);
            }

            lineHandlerTasks.add(createReadHandlerTask(msi, header, data, lineNumber, msiReadHandler));
        }

        // if there was no handler recovered, warn
        if (handlers.isEmpty()) {
            // no handlers for this column, or else only 1 handler that signifies unknown header
            // no handlers for this column, so skip it
            String message = "Line " + lineNumber + " (" + header + ") " +
                    "cannot be read - no handler can read this data.  Skipping.";

            // log a warning - we'll ignore this tag
            getLog().warn(message);

            // fire error item
            /*
            ErrorItem error =
                    ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                            .generateErrorItem(
                                    message, -1, this.getClass());
            fireErrorItemEvent(error);
            */
        }

        return lineHandlerTasks;
    }

    private Callable<Void> createReadHandlerTask(final MSI msi,
                                                 final String header,
                                                 final String[] data,
                                                 final int lineNumber,
                                                 final MSIReadHandler msiReadHandler) {
        return new Callable<Void>() {
            public Void call() throws ParseException {
                try {
                    // read the data with the given handler
                    msiReadHandler.read(header, data, msi, lineNumber, 0);
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
                        // set the column number (if not already set, defaults to 0)
                        if (item.getCol() == -1) {
                            item.setCol(0);
                        }
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
                return null;
            }
        };
    }
    
}
