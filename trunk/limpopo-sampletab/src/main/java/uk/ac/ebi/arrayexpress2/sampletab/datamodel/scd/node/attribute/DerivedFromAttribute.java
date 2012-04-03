package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

/**
 * This attribute represents where a sample has been derived from a sample in another submission.
 * 
 * If a sample has been derived from a sample in the same submission, then a parent/child 
 * relationship should be established between the relevant {@link SampleNode} objects.
 *  
 * @author faulcon
 *
 */
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
