package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class SexAttribute extends AbstractNamedAttribute {

    private static String name = "Sex";
    
    public SexAttribute(){
        super();
    }
    
    public SexAttribute(String attributeValue) {
        super(attributeValue);
    }

    //need to keep this getter because value being get has been overriden
    public String getAttributeType() {
        return name;
    }
}
