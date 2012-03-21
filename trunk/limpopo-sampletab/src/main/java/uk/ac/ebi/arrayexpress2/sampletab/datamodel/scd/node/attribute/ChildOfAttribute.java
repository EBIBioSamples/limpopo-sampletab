package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class ChildOfAttribute extends AbstractRelationshipAttribute {

    private static String name = "ChildOf";
    
    public ChildOfAttribute() {
        super();
    }
    
    public ChildOfAttribute(String value) {
        super(value);
    }

    //need to keep this getter because value being get has been overriden
    public String getAttributeType() {
        return name;
    }

}
