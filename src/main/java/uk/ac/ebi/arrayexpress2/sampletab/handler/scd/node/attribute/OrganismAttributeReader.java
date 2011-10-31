package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.NamedAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.OrganismAttribute;

public class OrganismAttributeReader extends NamedAttributeReader {

    protected String namedAttribute = "Organism";

    @Override
    public boolean canRead(String firstHeader) {
        return firstHeader.equals("organism");
    }
    
	@Override
	protected NamedAttribute getNewAttribute() {
		return new OrganismAttribute();
	}

}
