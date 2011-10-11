package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

public interface SCDAttributeReader {
	
    boolean canRead(String firstHeader);

    int assess(String[] header);

    public void readAttributes(String[] header,
                               String[] data,
                               SCD scd,
                               SCDNode scdNode,
                               int lineNumber,
                               int columnNumber)
            throws ParseException;
}
