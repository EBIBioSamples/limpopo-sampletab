package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;


/**
 * This class represents any named attribute. In time, this will become depreciated in favour of 
 * concrete alternatives (e.g. {@link MaterialAttribute}) but for the moment it is 
 * required to generate the variant SampleTab files that can be converted to AGE-Tab.
 * 
 * @author Adam Faulconbridge
 *
 */
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
