package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class SexAttribute extends NamedAttribute {

    public String name = "Sex";
    
    @Override
    public String getAttributeType() {
        return "Sex";
    }
}
