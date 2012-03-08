package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import java.util.ArrayList;
import java.util.List;


public class UnitAttribute extends AbstractNodeAttributeOntology {
    public String type;

    public String getAttributeType() {
        return "unit[" + type + "]";
    }

    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add("Unit[" + type + "]");
        if (getTermSourceREF() != null) {
            headersList.add("Term Source Ref");
        }
        if (getTermSourceID() != null) {
            headersList.add("Term Source ID");
        }
        String[] result = new String[headersList.size()];
        return headersList.toArray(result);
    }

    public String[] values() {
        List<String> valuesList = new ArrayList<String>();
        valuesList.add(getAttributeValue());
        if (getTermSourceREF() != null) {
            valuesList.add(getTermSourceREF());
        }
        if (getTermSourceID() != null) {
            valuesList.add(getTermSourceID());
        }
        String[] result = new String[valuesList.size()];
        return valuesList.toArray(result);
    }
}