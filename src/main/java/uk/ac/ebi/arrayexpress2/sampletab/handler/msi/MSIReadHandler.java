package uk.ac.ebi.arrayexpress2.sampletab.handler.msi;

import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.IllegalLineLengthException;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.AbstractHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.ReadHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.magetab.utils.MAGETABUtils;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;

public abstract class MSIReadHandler extends AbstractHandler implements ReadHandler<String, String[], MSI> {
    public boolean canRead(Object header) {
        return header instanceof String && canReadHeader((String) header);
    }

    public int getAllowedLength() {
        return -1;
    }
    
    public void read(String header, String[] data, MSI msi, int lineNumber, int columnNumber) throws ParseException {
        getLog().trace("MSI Handler '" + getClass().getSimpleName() + "' started reading");

        // check typed
        boolean isTyped = false;
        String type = "";
        if (header.contains("[") || header.contains("]")) {
            isTyped = true;
            type = MAGETABUtils.extractType(header);
        }

        // check cardinality
        if (getAllowedLength() != -1 && data.length > getAllowedLength()) {
            String message = "Supplied data exceeds allowed length: Max cardinality for " + header + " is " +
                    getAllowedLength() + ", this document contains " + data.length;

            ErrorItem error =
                    ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                            .generateErrorItem(
                                    message,
                                    23,
                                    this.getClass());

            throw new IllegalLineLengthException(false, message, error);
        }
        else {
            // fire listener start event
            fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

            int column = 1;
            try {
                // read
                if (data.length == 0) {
                    // read empty to ensure handler records that there was a header but no data vs. no header
                    if (isTyped) {
                        readValue(msi, "", lineNumber, type);
                    }
                    else {
                        readValue(msi, "", lineNumber);
                    }
                }
                else {
                    for (String value : data) {
                        if (isTyped) {
                            readValue(msi, value, lineNumber, type);
                        }
                        else {
                            readValue(msi, value, lineNumber);
                        }
                        column++;
                    }
                }

                // fire listener success event
                fireHandlingSucceededEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
            }
            catch (ParseException e) {
                // fire listener failed event
                fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                // update each item with relevent location info
                for (ErrorItem item : e.getErrorItems()) {
                    // set location, line, column number on error item
                    item.setLine(lineNumber);
                    item.setCol(column);
                }

                // and rethrow exception
                throw e;
            }
        }
        getLog().trace("MSI Handler '" + getClass().getSimpleName() + "' finished reading");
    }

    protected abstract boolean canReadHeader(String header);

    protected abstract void readValue(MSI msi, String value, int lineNumber, String... types)
            throws ParseException;
}
