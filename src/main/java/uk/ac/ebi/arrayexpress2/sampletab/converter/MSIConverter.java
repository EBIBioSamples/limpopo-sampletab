package uk.ac.ebi.arrayexpress2.sampletab.converter;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.converter.AbstractConverter;
import uk.ac.ebi.arrayexpress2.magetab.exception.ConversionException;
import uk.ac.ebi.arrayexpress2.magetab.handler.IConversionHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.HandlerLoader;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 29/03/11
 */
public class MSIConverter<O> extends AbstractConverter<MSI, O> {
    public void convert(MSI msi, O outputResource) throws ConversionException {
        // find any conversion handlers
        Collection<IConversionHandler<?, ?>> handlers = HandlerLoader.getHandlerLoader().getConversionHandlers();

        // use any appropriate handlers to convert
        for (IConversionHandler conversionHandler : handlers) {
            if (conversionHandler.canConvert(msi.getClass(), outputResource.getClass())) {
                conversionHandler.convert(msi, outputResource);
            }
        }
    }

    public void convert(MSI msi, O outputResource, ExecutorService service)
            throws ConversionException, InterruptedException {
        // now, create writing handler tasks
        for (Callable<Void> nextTask : createConversionHandlerTasks(msi, outputResource)) {
            service.submit(nextTask);
        }
    }

    protected Collection<Callable<Void>> createConversionHandlerTasks(MSI msi, O outputResource) {
        // find any convert handlers
        Collection<IConversionHandler<?, ?>> handlers = HandlerLoader.getHandlerLoader().getConversionHandlers();

        // create a callable for each handler
        Collection<Callable<Void>> writeHandlerTasks = new HashSet<Callable<Void>>();
        for (IConversionHandler conversionHandler : handlers) {
            if (conversionHandler.canConvert(msi.getClass(), outputResource.getClass())) {
                writeHandlerTasks.add(createConversionHandlerTask(msi, outputResource, conversionHandler));
            }
        }
        return writeHandlerTasks;
    }

    protected Callable<Void> createConversionHandlerTask(final MSI msi,
                                                       final O outputResource,
                                                       final IConversionHandler conversionHandler) {
        return new Callable<Void>() {
            public Void call() throws ConversionException {
                try {
                    conversionHandler.convert(msi, outputResource);
                }
                catch (ConversionException e) {
                    // get error item from the exception
                    for (ErrorItem item : e.getErrorItems()) {
                        // set type
                        if (e.isCriticalException()) {
                            item.setErrorType("conversion warning");
                        }
                        else {
                            item.setErrorType("conversion error");
                        }
                        // and fire the events
                        fireErrorItemEvent(item);
                    }
                    fireConversionFailedEvent(new ProgressEvent());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    fireConversionFailedEvent(new ProgressEvent());
                }
                return null;
            }
        };
    }
}
