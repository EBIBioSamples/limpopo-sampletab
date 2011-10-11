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
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.CharacteristicAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.UnitAttribute;


public class CharacteristicAttributeReader implements SCDAttributeReader {
    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public boolean canRead(String firstHeader) {
        return firstHeader.startsWith("characteristic");
    }

    public int assess(String[] header) {
        for (int i = 1; i < header.length; i++) {
            if (header[i].startsWith("unit")) {
                // ok
            }
            else if (header[i].equals("termsourceref")) {
                // ok
            }
            else if (header[i].equals("termaccessionid")) {
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
        // CharacteristicsAttribute to create
        CharacteristicAttribute characteristic;

        if (canRead(header[0])) {
            // make sure attribute is not empty
            if (data[0] != null && !data[0].equals("")) {
                // first row, so make a new attribute node
                characteristic = new CharacteristicAttribute();

                String type =
                        header[0].substring(header[0].lastIndexOf("[") + 1,
                                            header[0].lastIndexOf("]"));
                characteristic.setAttributeValue(data[0]);
                characteristic.type = type;

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
                                    unit.termSourceREF = data[j];
                                }
                                else if (header[j].equals("termsourceid")) {
                                    unit.termSourceID = data[j];
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
                            characteristic.unit = unit;
                        }
                    }
                    else if (header[i].equals("termsourceref")) {
                        characteristic.termSourceREF = data[i];
                    }
                    else if (header[i].equals("termsourceid")) {
                        characteristic.termSourceID = data[i];
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