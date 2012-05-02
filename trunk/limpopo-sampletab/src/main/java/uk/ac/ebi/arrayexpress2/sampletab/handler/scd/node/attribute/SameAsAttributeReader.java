package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SameAsAttribute;

public class SameAsAttributeReader extends RelationshipAttributeReader {

    protected String namedAttribute = "Same As";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("sameas");
    }
    
	@Override
	protected AbstractNodeAttribute getNewAttribute() {
		return new SameAsAttribute();
	}

}
