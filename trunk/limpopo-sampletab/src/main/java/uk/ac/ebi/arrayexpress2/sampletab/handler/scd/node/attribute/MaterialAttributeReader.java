package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.MaterialAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.NamedAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.OrganismAttribute;

public class MaterialAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Material";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("material");
    }
    
	@Override
	protected NamedAttribute getNewAttribute() {
		return new MaterialAttribute();
	}

}
