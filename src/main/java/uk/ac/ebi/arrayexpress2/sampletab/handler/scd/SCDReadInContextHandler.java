package uk.ac.ebi.arrayexpress2.sampletab.handler.scd;

import java.util.Collection;

import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.IllegalLineLengthException;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.exception.UnmatchedTagException;
import uk.ac.ebi.arrayexpress2.magetab.handler.HandlerLoader;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

/**
 * An implementation of an SCDReadHandler that defines two extra methods that allow extra contextual data to be passed
 * to concrete implementations.  In places in the SCD spec, nodes cannot be read entirely in isolation: examples of
 * this are Factor Values and Protocols.  Neither of these things are true "nodes" but do, in fact, behave like nodes in
 * the spreadsheet rendering.  Factor values are attributes that should be attached to the last Hybridization, assay or
 * scan node but can move downstream of this in the graph.  Protocols are edges but are modelled as protocol
 * applications, where a protocol application node is a unique node described by the node upstream of it, the protocol
 * references and any parameter values supplied.
 * <p/>
 * For these reasons, both Protocol handlers and Factor Value handlers require additional context information: the
 * columns prior to the FV or Hyb column must also be passed.  This class exists to provide a common mechanism for
 * passing this extra context information by defining a {@link #readFrom(String[], String[],
 * uk.ac.ebi.arrayexpress2.magetab.datamodel.SCD, int, int)} method that behaves in the same manner as {@link
 * uk.ac.ebi.arrayexpress2.magetab.handler.ReadHandler#read(Object, Object, Object, int, int)} but allows a starting
 * index to be passed. Implementations should start from the supplied index (this will be the factor value/protocol
 * column) and read backwards or forwards as necessary.
 *
 * @author Tony Burdett
 * @date 15/02/11
 */
public abstract class SCDReadInContextHandler extends SCDReadHandler {
    public boolean canReadHeader(String[] header) {
        return canReadFrom(header, 0);
    }

    @Override
    public void read(String[] header, String[] data, SCD scd, int lineNumber, int columnNumber)
            throws ParseException {
        throw new UnsupportedOperationException(
                "This handler '" + getClass().getSimpleName() + "' requires extra context information - " +
                        "you should utilise the readFrom() method instead");
    }

    /**
     * Cause the handler to read some of the supplied data (usually starting from startIndex) into the supplied target
     * object, using additional context information where appropriate.
     *
     * @param header       the header identifying the type of the data
     * @param data         the data to be read
     * @param scd         the object that the data will be read into
     * @param lineNumber   the line number this data was read from
     * @param columnNumber the index at which we want to commence reading from
     * @throws uk.ac.ebi.arrayexpress2.magetab.exception.ParseException
     *          if parsing failed
     */
    public void readFrom(String[] header, String[] data, SCD scd, int lineNumber, int columnNumber)
            throws ParseException {
        // fire listener start event
        fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        getLog().trace("SCD Handler '" + getClass().getSimpleName() + "' started reading");

        // if every element of data is empty, read nothing
        boolean noData = true;
        for (int i = 0; i < assessFrom(header, columnNumber); i++) {
            String d = data[i];
            if (!d.isEmpty()) {
                noData = false;
                break;
            }
        }

        if ((header.length - columnNumber) < 1) {
            String message =
                    "There is no data to be read!";

            ErrorItem error =
                    ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                            .generateErrorItem(
                                    message,
                                    -1,
                                    this.getClass());

            // fire listener failed event
            fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

            // set location, line, column number on error item
            error.setLine(lineNumber);
            error.setCol(columnNumber);

            throw new ParseException(false, message);
        }
        else {
            if (!canReadFrom(header, columnNumber)) {
                String message =
                        "Handler " + getClass().getSimpleName() + " cannot read this data, " +
                                "because the header starts with '" + header[0] + "'";

                ErrorItem error =
                        ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                                .generateErrorItem(
                                        message,
                                        -1,
                                        this.getClass());

                // fire listener failed event
                fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                // set location, line, column number on error item
                error.setLine(lineNumber);
                error.setCol(columnNumber);

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
                                        -1,
                                        this.getClass());

                // fire listener failed event
                fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                // set location, line, column number on error item
                error.setLine(lineNumber);
                error.setCol(columnNumber);

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
                    // fire listener start event
                    fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));

                    // everything ok, so read
                    readDataFrom(header, data, scd, lineNumber, columnNumber);

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
                        item.setCol(columnNumber);
                    }

                    // and rethrow exception
                    throw e;
                }
            }
        }
        getLog().trace("SCD Handler '" + getClass().getSimpleName() + "' finished reading");
    }

    /**
     * Determine whether this handler can read the data referenced by this header, starting from the supplied start
     * index.  The header in an SCD file is the column heading for each tab-separated column.  This specialised form of
     * {@link #canReadHeader(String[])} is designed for handlers that require additional context information (handlers
     * for Factor Values and Protocols, usually).
     * <p/>
     * The header should be passed to the handler prior to reading to determine whether this handler can handle this
     * line.
     *
     * @param header     the first token on a line, or the column heading, which is the string describing this line
     *                   contents
     * @param startIndex the index that designates where the data starts
     * @return true if this handler can handle this tag, false otherwise
     */
    public abstract boolean canReadFrom(String[] header, int startIndex);

    /**
     * Return the index of the last header this handler can read from in the supplied header, starting from the given
     * start index. This can be used to determine the start and finish indices that a handler can read from/to.
     *
     * @param header       the header to assess
     * @param columnNumber the index of the first column where this data starts
     * @return the last column index that can be read
     */
    public abstract int assessFrom(String[] header, int columnNumber);

    /**
     * Performs the unit of work to read the data into the internal datamodel. Override this method in implementations,
     * so that you don't need to implement status updating and checking code.  Implementations should determine how much
     * data should be read from the data queue - access the data that still needs to be read by calling
     *
     * @param header       the header identifying the data to read
     * @param data         the data to read
     * @param scd         the SCD to add data to
     * @param lineNumber   the line number this data was read from
     * @param columnNumber the first column of the data being read
     * @throws ParseException if the header cannot be parsed or there was an error reading the value
     */
    public abstract void readDataFrom(String[] header,
                                      String[] data,
                                      SCD scd,
                                      int lineNumber,
                                      int columnNumber) throws ParseException;

    /**
     * Used for adding nodes that are upstream of the current node in the SCD graph.  This is only required when
     * handlers need to read backwards in the SCD graph to resolve their parents.  Special node types that extend
     * <code>SCDReadInContextHandler</code> to provide the ability to read-back should use this method if they discover
     * a node reference to a parent node that has not yet been added to the SCD graph.
     * <p/>
     * This method actually creates a nested handler for generating the part of the SCD that is described by this
     * header and data.
     *
     * @param scd         the scd to query
     * @param nodeName     the name of the node
     * @param nodeType     the entry in the header
     * @param lineNumber   the line being read
     * @param columnNumber the column being read
     * @return the node that was fetched or created if absent
     * @throws uk.ac.ebi.arrayexpress2.magetab.exception.ParseException
     *          if the node could not be created or retrieved
     */
    protected SCDNode handleUpstreamNode(SCD scd, String nodeName, String nodeType, int lineNumber, int columnNumber)
            throws ParseException {
        SCDNode upstreamNode = scd.getNode(nodeName, nodeType);

        if (upstreamNode == null) {
            // couldn't retrieve, not yet added so create
            String[] headerPart = new String[]{nodeType};
            String[] dataPart = new String[]{nodeName};

            // get the handler(s) for this part of the graph
            Collection<SCDReadHandler> handlers =
                    HandlerLoader.getHandlerLoader().getReadHandlersOfType(SCDReadHandler.class, headerPart);

            for (final SCDReadHandler handler : handlers) {
                // read the data with the given handler, line and column numbers aren't relevant as they'll be overwritten
                if (handler instanceof SCDReadInContextHandler) {
                    ((SCDReadInContextHandler) handler).readFrom(headerPart, dataPart, scd, lineNumber, columnNumber);
                }
                else {
                    handler.read(headerPart, dataPart, scd, lineNumber, columnNumber);
                }
            }
        }

        // now retrieve again
        upstreamNode = scd.getNode(nodeName, nodeType);
        if (upstreamNode == null) {
            // still null, couldn't be created so throw a parse exception
            throw new ParseException("Upstream node '" + nodeName + "' [" + nodeType + "] could not be created");
        }
        else {
            return upstreamNode;
        }
    }

    /**
     * Used for adding nodes that are upstream of the current node in the SCD graph.  This is only required when
     * handlers need to read backwards in the SCD graph to resolve their parents.  Special node types that extend
     * <code>SCDReadInContextHandler</code> to provide the ability to read-back should use this method if they discover
     * a node reference to a parent node that has not yet been added to the SCD graph.
     *
     * @param scd      the scd to query
     * @param nodeName  the name of the node
     * @param nodeClass the class of the node required
     * @return the node that was fetched or created if absent
     * @throws uk.ac.ebi.arrayexpress2.magetab.exception.ParseException
     *          if the node could not be created or retrieved
     */

    protected <T extends SCDNode> T fetchUpstreamNode(SCD scd, String nodeName, Class<T> nodeClass)
            throws ParseException {
        // find our anchor node
        T upstreamNode = scd.getNode(nodeName, nodeClass);

        try {
            // maybe parsing downstream node before anchor has been inserted, so create if missing
            if (upstreamNode == null) {
                upstreamNode = nodeClass.newInstance();
                upstreamNode.setNodeName(nodeName);
                scd.addNode(upstreamNode);
            }
        }
        catch (InstantiationException e) {
            getLog().error("Failed to instantiate a new SCDNode of type " + nodeClass.getSimpleName());
            throw new ParseException(e);
        }
        catch (IllegalAccessException e) {
            getLog().error("Failed to instantiate a new SCDNode of type " + nodeClass.getSimpleName());
            throw new ParseException(e);
        }

        return upstreamNode;
    }
}
