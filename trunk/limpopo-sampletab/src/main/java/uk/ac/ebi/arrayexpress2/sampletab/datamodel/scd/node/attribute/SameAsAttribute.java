package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

/**
 * This attribute represents where a sample is thought to be the same as a sample in another submission.
 * 
 * @author faulcon
 *
 */
public class SameAsAttribute extends AbstractRelationshipAttribute {

    private static String name = "Same As";
    
    public SameAsAttribute() {
        super();
    }
    
    public SameAsAttribute(String value) {
        super(value);
    }

    //need to keep this getter because value being get has been overridden
    public String getAttributeType() {
        return name;
    }

}
