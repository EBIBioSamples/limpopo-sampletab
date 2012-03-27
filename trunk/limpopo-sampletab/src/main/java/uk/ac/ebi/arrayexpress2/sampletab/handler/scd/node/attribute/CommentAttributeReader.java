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
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.CommentAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.UnitAttribute;


public class CommentAttributeReader implements SCDAttributeReader {
    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public boolean canRead(String firstHeader) {
        return firstHeader.startsWith("comment");
    }

    public int assess(String[] header) {
        for (int i = 1; i < header.length; i++) {
            if (header[i].startsWith("unit")) {
                // ok
            }
            else if (header[i].equals("termsourceref")) {
                // ok
            }
            else if (header[i].equals("termsourceid")) {
                // ok
            }
            else {
                // got to something we don't recognise, so this is the end
                return i - 1;
            }
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
        // CommentAttribute to create
    	CommentAttribute attribute;

        if (canRead(header[0])) {
            // make sure attribute is not empty
            if (data[0] != null && !data[0].equals("")) {
                // first row, so make a new attribute node
            	attribute = new CommentAttribute();

                String type =
                        header[0].substring(header[0].lastIndexOf("[") + 1,
                                            header[0].lastIndexOf("]"));
                attribute.setAttributeValue(data[0]);
                attribute.type = type;

                // now do the rest
                for (int i = 1; i < data.length;) {
                    if (header[i].startsWith("unit")) {
                        String unit_type =
                                header[i].substring(header[i].lastIndexOf("[") + 1,
                                                    header[i].lastIndexOf("]"));
                        if (data[i] != null && !data[i].equals("")) {
                            UnitAttribute unit = new UnitAttribute();
                            unit.setAttributeValue(data[i]);
                            unit.type = unit_type;

                            for (int j = i + 1; j < data.length; j++) {
                                if (header[j].equals("termsourceref")) {
                                    unit.setTermSourceREF(data[j]);
                                }
                                else if (header[j].equals("termsourceid")) {
                                    unit.setTermSourceID(data[j]);
                                }
                                else if (header[i + 1].equals("")) {
                                    // skip the case where the header is an empty string
                                }
                                else {
                                    break;
                                }

                                // update i to j
                                i = j;
                            }

                            // and set the unit
                            attribute.unit = unit;
                        }
                    }
                    else if (header[i].equals("termsourceref")) {
                    	attribute.setTermSourceREF(data[i]);
                    }
                    else if (header[i].equals("termsourceid")) {
                    	attribute.setTermSourceID(data[i]);
                    }
                    else if (header[i].equals("")) {
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