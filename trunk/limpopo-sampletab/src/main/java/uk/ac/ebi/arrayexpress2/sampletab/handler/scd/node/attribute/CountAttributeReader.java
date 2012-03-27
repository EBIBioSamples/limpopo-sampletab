package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNamedAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.CountAttribute;

public class CountAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Count";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("count");
    }
    
	@Override
	protected AbstractNamedAttribute getNewAttribute() {
		return new CountAttribute();
	}

}
