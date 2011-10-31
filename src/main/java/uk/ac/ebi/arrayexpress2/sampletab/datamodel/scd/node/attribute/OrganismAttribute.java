package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class OrganismAttribute extends NamedAttribute {

    public String name = "Organism";
    
    @Override
    public String getAttributeType() {
        return "Organism";
    }
}
