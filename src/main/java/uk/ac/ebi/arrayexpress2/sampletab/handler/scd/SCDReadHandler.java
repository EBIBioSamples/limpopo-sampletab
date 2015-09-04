package uk.ac.ebi.arrayexpress2.sampletab.handler.scd;

import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.IllegalLineLengthException;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.exception.UnmatchedTagException;
import uk.ac.ebi.arrayexpress2.magetab.handler.AbstractHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.IReadHandler;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.magetab.utils.MAGETABUtils;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.SCDAttributeReader;

public abstract class SCDReadHandler extends AbstractHandler implements IReadHandler<String[], String[], SCD> {

    public boolean canRead(Object header) {
        return header instanceof String[] && canReadHeader((String[]) header);
    }

    public void read(String[] header, String[] data, SCD scd, int lineNumber, int columnNumber)
            throws ParseException {
        // fire listener start event
        fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        getLog().trace("SCD Handler '" + getClass().getSimpleName() + "' started reading");

        // if every element of data is empty, read nothing
        boolean noData = true;
        if (data.length > 0){
            for (int i = 0; i < assess(header) && i < data.length; i++) {
                String d = data[i];
                if (!d.isEmpty()) {
                    noData = false;
                    break;
                }
            }
        }

        if (header.length < 1) {
            String message =
                    "There is no data to be read!";

            ErrorItem error =
                    ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                            .generateErrorItem(
                                    message,
                                    501,
                                    this.getClass());

            // fire listener failed event
            fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

            // set location, line, column number on error item
            error.setLine(lineNumber);
            error.setCol(columnNumber);

            throw new ParseException(false, message);
        }
        else {
            if (!canReadHeader(header)) {
                String message =
                        "Handler " + getClass().getSimpleName() + " cannot read this data, " +
                                "because the header starts with '" + header[0] + "'";

                ErrorItem error =
                        ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                                .generateErrorItem(
                                        message,
                                        503,
                                        this.getClass());

                // fire listener failed event
                fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                // set location, line, column number on error item
                error.setLine(lineNumber);
                error.setCol(columnNumber);

                System.out.println(message+" line "+lineNumber+" col "+columnNumber);
                // tag must be wrong
                throw new UnmatchedTagException(false, message, error);
            }
            else if (header.length < data.length) {
                String message =
                        "Wrong number of elements - header contains " + header.length + " elements " +
                                "whereas data contains " + data.length + " elements";

                ErrorItem error =
                        ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                                .generateErrorItem(
                                        message,
                                        515,
                                        this.getClass());

                // fire listener failed event
                fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                // set location, line, column number on error item
                error.setLine(lineNumber);
                error.setCol(columnNumber);
                System.out.println(message+" line "+lineNumber+" col "+columnNumber);

                throw new IllegalLineLengthException(false, message, error);
            }
            else if (noData) {
                // there is no data to be read - so we can just skip this item
                getLog().debug("There is no data to be read for '" + header[0] + " at column " + columnNumber);

                // fire listener success event
                fireHandlingSucceededEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
            }
            else {
                try {
                    // everything ok, so read
                    readData(header, data, scd, lineNumber, columnNumber);

                    // fire listener success event
                    fireHandlingSucceededEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
                }
                catch (ParseException e) {
                    // fire listener failed event ...
                    fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                    // ... update each item with relevant location info ...
                    for (ErrorItem item : e.getErrorItems()) {
                        // set location, line, column number on error item
                        item.setLine(lineNumber);
                        item.setCol(columnNumber);
                    }

                    // ... and re-throw exception
                    throw e;
                }
            }
        }
        getLog().trace("SCD Handler '" + getClass().getSimpleName() + "' finished reading");

    }

    protected int assessAttribute(SCDAttributeReader reader, String[] header, int startIndex) {
        String[] attrHeader = MAGETABUtils.extractRange(header, startIndex, header.length);
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (String s : attrHeader) {
            sb.append(s).append(",");
        }
        sb.append("]");
        getLog().debug("Assessing attributes from header: '" + sb.toString() + "'");
        return reader.assess(attrHeader);
    }

    protected int readAttribute(SCDAttributeReader reader,
                                String[] header,
                                String[] data,
                                SCD scd,
                                SCDNode parentNode,
                                int lineNumber,
                                int columnNumber,
                                int startIndex) throws ParseException {
        String[] attrHeader =
                MAGETABUtils.extractRange(header, startIndex, header.length);
        String[] attrData =
                MAGETABUtils.extractRange(data, startIndex, data.length);
        // read the attributes
        int endIndex = reader.assess(attrHeader);
        reader.readAttributes(attrHeader, attrData, scd, parentNode, lineNumber, columnNumber);
        return endIndex;
    }

    protected void updateChildNode(String[] header, String[] data, SCDNode node, int valueIndex) {
        // check child node type
        // loop over values until we get to something with a value present
        int i = valueIndex;
        while (i < data.length) {
            // value present?
            if (data[i] == null || data[i].equals("")) {
                // no value, continue
                i++;
            }
            else {
                if (header[i].endsWith("name") ||
                        (header[i].endsWith("ref") &&
                                !header[i].endsWith("termsourceref")) ||
                        header[i].endsWith("file")) {
                    // nodes end with "name", "ref" or "file" strings, so this is the child node
                    break;
                }
                else {
                    // this is not a recognised header node
                    i++;
                }
            }
        }

        if (i < data.length) {
            // add child node value
            String childNodeType = header[i];
            String childNodeValue;
            childNodeValue = data[i];
            node.addChildNode(childNodeType, childNodeValue);
        }
    }

    /**
     * Determine whether this handler can read the data referenced by this header.  Typically, the header in an IDF file
     * (or ADF header) is the first token on any given line, and the header in an SCD file (or ADF graph part) is the
     * column heading for each tab-separated column.  Other types of data referencing can be supplied - for example, an
     * XML type reader might pass the tag qname as the header.
     * <p/>
     * The header should be passed to the handler prior to reading to determine whether this handler can handle this
     * line.
     *
     * @param header the first token on a line, or the column heading, which is the string describing this line
     *               contents
     * @return true if this handler can handle this tag, false otherwise
     */
    protected abstract boolean canReadHeader(String[] header);

    /**
     * Return the index of the last header this handler can read from in the supplied header. This can be used to
     * determine the start and finish indices that a handler can read from/to.
     *
     * @param header the header to assess
     * @return the last column index that can be read
     */
    public abstract int assess(String[] header);

    /**
     * Performs the unit of work to read the data into the internal datamodel. Override this method in implementations,
     * so that you don't need to implement status updating and checking code.  Implementations should determine how much
     * data should be read from the data queue - access the data that still needs to be read by calling
     *
     * @param header       the header identifying the data to read
     * @param data         the data to read
     * @param scd         the SCD to add data to
     * @param lineNumber   the lin number this data was read from
     * @param columnNumber the first column of the data being read
     * @throws ParseException if the header cannot be parsed or there was an error reading the value
     */
    protected abstract void readData(String[] header, String[] data, SCD scd, int lineNumber, int columnNumber)
            throws ParseException;
}
