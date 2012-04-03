package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class DerivedFromAttribute extends AbstractRelationshipAttribute {

    private static String name = "Derived From";
    
    public DerivedFromAttribute() {
        super();
    }
    
    public DerivedFromAttribute(String value) {
        super(value);
    }

    //need to keep this getter because value being get has been overridden
    public String getAttributeType() {
        return name;
    }

}
