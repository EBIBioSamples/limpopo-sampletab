package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class NamedAttribute extends AbstractNamedAttribute {

    private String name;
    
    public NamedAttribute() {
        super();
    }

    public NamedAttribute(String name, String value) {
        super(value);
        this.name = name;
    }

    public String getAttributeType() {
        return this.name;
    }

}
