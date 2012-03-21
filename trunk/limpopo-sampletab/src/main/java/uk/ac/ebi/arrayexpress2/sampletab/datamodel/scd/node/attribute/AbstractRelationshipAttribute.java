package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRelationshipAttribute extends AbstractNodeAttribute {

    public AbstractRelationshipAttribute(String value){
        super();
        this.setAttributeValue(value);
    }

    public AbstractRelationshipAttribute() {
        super();
    }

    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add(getAttributeType());
        String[] result = new String[headersList.size()];
        return headersList.toArray(result);
    }

    public String[] values() {
        List<String> valuesList = new ArrayList<String>();
        valuesList.add(getAttributeValue());
        String[] result = new String[valuesList.size()];
        return valuesList.toArray(result);
    }

}
