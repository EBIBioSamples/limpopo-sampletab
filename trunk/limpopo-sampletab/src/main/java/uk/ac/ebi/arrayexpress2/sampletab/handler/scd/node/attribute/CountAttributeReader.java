package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.CountAttribute;

public class CountAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Count";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("count");
    }
    
	@Override
	protected AbstractNodeAttribute getNewAttribute() {
		return new CountAttribute();
	}

}
