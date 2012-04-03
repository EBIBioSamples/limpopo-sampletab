package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.MaterialAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNamedAttribute;

public class DerivedFromAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Derived From";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("derivedfrom");
    }
    
	@Override
	protected AbstractNamedAttribute getNewAttribute() {
		return new MaterialAttribute();
	}

}
