package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class CountAttribute extends AbstractNamedAttribute {

    private static String name = "Count";
    
    public CountAttribute(){
        super();
    }
    
    public CountAttribute(int value) {
        super(""+value);
    }

    //need to keep this getter because value being get has been overriden
    public String getAttributeType() {
        return name;
    }
}
