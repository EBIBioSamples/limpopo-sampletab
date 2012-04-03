package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.MaterialAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNamedAttribute;

public class ChildOfAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Child Of";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("childof");
    }
    
	@Override
	protected AbstractNamedAttribute getNewAttribute() {
		return new MaterialAttribute();
	}

}
