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
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNamedAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractRelationshipAttribute;


public abstract class RelationshipAttributeReader implements SCDAttributeReader {
    private Logger log = LoggerFactory.getLogger(getClass());
    
    protected String namedAttribute = null;
    protected abstract AbstractNodeAttribute getNewAttribute();
    
    protected Logger getLog() {
        return log;
    }

    public boolean canRead(String firstHeader) {
        return firstHeader.equals(namedAttribute);
    }

    public int assess(String[] header) {
        //only one column for this attribute
        return 0;
    }
    

    public void readAttributes(String[] header,
                               String[] data,
                               SCD scd,
                               SCDNode parentNode,
                               int lineNumber,
                               int columnNumber) throws ParseException {
        AbstractRelationshipAttribute attribute;
        
        if (canRead(header[0])) {
            // make sure attribute is not empty
            if (data[0] != null && !data[0].equals("")) {
                // first row, so make a new attribute node
            	attribute = (AbstractRelationshipAttribute) getNewAttribute();
            	attribute.setAttributeValue(data[0]);
            	
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