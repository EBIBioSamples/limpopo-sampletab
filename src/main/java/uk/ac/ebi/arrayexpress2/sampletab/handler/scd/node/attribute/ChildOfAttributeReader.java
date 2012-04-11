package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import org.mged.magetab.error.ErrorCode;
import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.exception.UnmatchedTagException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.ChildOfAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.DatabaseAttribute;

public class ChildOfAttributeReader implements SCDAttributeReader {
    private Logger log = LoggerFactory.getLogger(getClass());
        
    protected AbstractNodeAttribute getNewAttribute() {
        return new ChildOfAttribute();
    }
    
    protected Logger getLog() {
        return log;
    }

    public boolean canRead(String firstHeader) {
        return firstHeader.equals("childof");
    }

    public int assess(String[] header) {
        for (int i = 1; i < header.length; i++) {
            // got to something we don't recognise, so this is the end
            return i - 1;
        }

        // iterated over every column, so must have reached the end
        return header.length - 1;
    }
    

    public void readAttributes(String[] header,
                               String[] data,
                               SCD scd,
                               SCDNode parentNode,
                               int lineNumber,
                               int columnNumber) throws ParseException {
        ChildOfAttribute attribute;

        if (canRead(header[0])) {
            // make sure attribute is not empty
            if (data[0] != null && !data[0].equals("")) {
                // first row, so make a new attribute node
                attribute = (ChildOfAttribute) getNewAttribute();
                attribute.setAttributeValue(data[0]);
                
                // now do the rest
                for (int i = 1; i < data.length;) {
                    if (header[i].equals("")) {
                        // skip the case where the header is an empty string
                    }
                    else {
                        // got to something we don't recognise, so this is the end
                        break;
                    }
                    i++;
                }
                
                parentNode.addAttribute(attribute);
            }
        }
        else {
            String message = "Attribute reader '" + getClass().getSimpleName() + "' " +
                    "cannot read attributes starting with " + header[0];

            ErrorItem error =
                    ErrorItemFactory.getErrorItemFactory(getClass().getClassLoader())
                            .generateErrorItem(
                                    message,
                                    ErrorCode.UNKNOWN_SDRF_HEADING,
                                    this.getClass());

            throw new UnmatchedTagException(false, message, error);
        }
    }
}