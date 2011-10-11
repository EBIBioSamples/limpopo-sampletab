package uk.ac.ebi.arrayexpress2.sampletab.converter;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.converter.AbstractConverter;
import uk.ac.ebi.arrayexpress2.magetab.exception.ConversionException;
import uk.ac.ebi.arrayexpress2.magetab.handler.ConversionHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.HandlerLoader;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.magetab.listener.ProgressEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class SampleTabConverter<O> extends AbstractConverter<SampleData, O> {
    private final MSIConverter<O> msiConverter;
    private final SCDConverter<O> scdConverter;

    public SampleTabConverter() {
        this.msiConverter = new MSIConverter<O>();
        this.scdConverter = new SCDConverter<O>();
    }

    public void convert(SampleData sampledata, O outputResource) throws ConversionException {
        // check for any convert handlers that convert the investigation as a whole
        // find any convert handlers
        Collection<ConversionHandler<?, ?>> handlers = HandlerLoader.getHandlerLoader().getConversionHandlers();

        // invoke each handler
        for (final ConversionHandler conversionHandler : handlers) {
            // is it a convert handler, and can it handle this investigation?
            if (conversionHandler.canConvert(sampledata, outputResource)) {
                try {
                    conversionHandler.convert(sampledata, outputResource);
                }
                catch (ConversionException e) {
                    // get error item from the exception
                    for (ErrorItem item : e.getErrorItems()) {
                        // set type
                        if (e.isCriticalException()) {
                            item.setErrorType("parse warning");
                        }
                        else {
                            item.setErrorType("parse error");
                        }
                        // and fire the events
                        fireErrorItemEvent(item);
                        fireConversionFailedEvent(new ProgressEvent());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    fireConversionFailedEvent(new ProgressEvent());
                }
            }
        }

        // and delegate msi and scd conversion
        msiConverter.convert(sampledata.msi, outputResource);
        scdConverter.convert(sampledata.scd, outputResource);
    }

    public void convert(final SampleData sampledata, final O outputResource, ExecutorService service)
            throws ConversionException, InterruptedException {
        // check for any convert handlers that convert the investigation as a whole
        // find any convert handlers
        Collection<ConversionHandler<?, ?>> handlers = HandlerLoader.getHandlerLoader().getConversionHandlers();

        // create a callable for each handler
        for (final ConversionHandler conversionHandler : handlers) {
            // is it a convert handler, and can it handle this investigation?
            if (conversionHandler.canConvert(sampledata, outputResource)) {
                service.submit(new Callable<Void>() {
                    public Void call() throws Exception {
                        try {
                            conversionHandler.convert(sampledata, outputResource);
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
                });
            }
        }

        // and delegate msi and scd conversion
        msiConverter.convert(sampledata.msi, outputResource, service);
        scdConverter.convert(sampledata.scd, outputResource, service);
    }
    
	@Override
    public void addErrorItemListener(ErrorItemListener listener) {
        super.addErrorItemListener(listener);
        msiConverter.addErrorItemListener(listener);
        scdConverter.addErrorItemListener(listener);
    }
}
