package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.DerivedFromAttribute;

public class DerivedFromAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Derived From";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("derivedfrom");
    }
    
	@Override
	protected AbstractNodeAttribute getNewAttribute() {
		return new DerivedFromAttribute();
	}

}
