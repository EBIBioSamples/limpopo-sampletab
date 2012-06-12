package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChildOfAttribute extends AbstractRelationshipAttribute {

    private static String name = "Child Of";

    private Logger log = LoggerFactory.getLogger(getClass());

    public ChildOfAttribute() {
        super();
        //log.warn("ChildOf attributes may be deprecated in the future.");
    }
    
    public ChildOfAttribute(String value) {
        super(value);
    }

    //need to keep this getter because value being get has been overridden
    public String getAttributeType() {
        return name;
    }

}
