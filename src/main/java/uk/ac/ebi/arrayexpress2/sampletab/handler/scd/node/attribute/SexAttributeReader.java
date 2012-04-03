package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SexAttribute;

public class SexAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Sex";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("sex");
    }
    
	@Override
	protected AbstractNodeAttribute getNewAttribute() {
		return new SexAttribute();
	}

}
