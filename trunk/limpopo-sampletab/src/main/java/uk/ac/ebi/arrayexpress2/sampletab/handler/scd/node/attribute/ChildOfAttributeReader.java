package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.ChildOfAttribute;

public class ChildOfAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Child Of";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("childof");
    }
    
	@Override
	protected AbstractNodeAttribute getNewAttribute() {
		return new ChildOfAttribute();
	}

}
