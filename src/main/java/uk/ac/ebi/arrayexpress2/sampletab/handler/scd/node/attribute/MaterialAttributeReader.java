package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.MaterialAttribute;

public class MaterialAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Material";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("material");
    }
    
	@Override
	protected AbstractNodeAttribute getNewAttribute() {
		return new MaterialAttribute();
	}

}
