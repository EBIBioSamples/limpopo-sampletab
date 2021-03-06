package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class MaterialAttribute extends AbstractNamedAttribute {

    public static String name = "Material";
    
    public MaterialAttribute(){
        super();
    }

    public MaterialAttribute(String attributeValue) {
        super(attributeValue);
    }

    //need to keep this getter because value being get has been overriden
    public String getAttributeType() {
        return name;
    }
}
