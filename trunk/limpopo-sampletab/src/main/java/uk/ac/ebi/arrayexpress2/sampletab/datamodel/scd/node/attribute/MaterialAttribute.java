package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class MaterialAttribute extends NamedAttribute {

    public String name = "Material";
    
    @Override
    public String getAttributeType() {
        return "Material";
    }
}
