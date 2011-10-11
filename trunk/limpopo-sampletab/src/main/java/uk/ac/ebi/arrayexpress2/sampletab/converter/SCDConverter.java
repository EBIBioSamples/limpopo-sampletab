package uk.ac.ebi.arrayexpress2.sampletab.converter;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.converter.AbstractConverter;
import uk.ac.ebi.arrayexpress2.magetab.exception.ConversionException;
import uk.ac.ebi.arrayexpress2.magetab.handler.ConversionHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.HandlerLoader;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 29/03/11
 */
public class SCDConverter<O> extends AbstractConverter<SCD, O> {
    public void convert(SCD scd, O outputResource) throws ConversionException {
        // find any conversion handlers
        Collection<ConversionHandler<?, ?>> handlers = HandlerLoader.getHandlerLoader().getConversionHandlers();

        // use any appropriate handlers to convert the whole scd
        for (ConversionHandler conversionHandler : handlers) {
            if (conversionHandler.canConvert(scd, outputResource)) {
                conversionHandler.convert(scd, outputResource);
            }
        }

        // and use any appropriate handlers to validate each scd node
        for (SCDNode node : scd.getAllNodes()) {
            for (ConversionHandler conversionHandler : handlers) {
                if (conversionHandler.canConvert(node, outputResource)) {
                    conversionHandler.convert(node, outputResource);
                }
            }
        }

    }

    public void convert(SCD scd, O outputResource, ExecutorService service)
            throws ConversionException, InterruptedException {
        // now, create writing handler tasks
        for (Callable<Void> nextTask : createConversionHandlerTasks(scd, outputResource)) {
            service.submit(nextTask);
        }
    }

    private Collection<Callable<Void>> createConversionHandlerTasks(SCD scd, O outputResource) {
        // find any convert handlers
        Collection<ConversionHandler<?, ?>> handlers = HandlerLoader.getHandlerLoader().getConversionHandlers();

        // create a callable for each handler
        Collection<Callable<Void>> conversionHandlerTasks = new HashSet<Callable<Void>>();
        for (ConversionHandler<?, ?> conversionHandler : handlers) {
            if (conversionHandler.canConvert(scd, outputResource)) {
                conversionHandlerTasks.add(createConversionHandlerTask(scd, outputResource, conversionHandler));
            }
        }

        // create a callable for each handler to convert each scd node
        for (SCDNode node : scd.getAllNodes()) {
            for (ConversionHandler conversionHandler : handlers) {
                if (conversionHandler.canConvert(node, outputResource)) {
                    conversionHandlerTasks.add(createConversionHandlerTask(scd, outputResource, conversionHandler));
                }
            }
        }

        return conversionHandlerTasks;
    }

    private Callable<Void> createConversionHandlerTask(final SCD scd,
                                                       final O outputResource,
                                                       final ConversionHandler conversionHandler) {
        return new Callable<Void>() {
            public Void call() throws ConversionException {
                try {
                    conversionHandler.convert(scd, outputResource);
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
