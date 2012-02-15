package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacteristicAttribute extends AbstractNodeAttribute {
    public String type;
    public UnitAttribute unit;
    public String termSourceREF;
    public String termSourceID;
    
    public CharacteristicAttribute(){
        super();
    }

    public CharacteristicAttribute(String name, String value){
        super();
        this.type = name;
        this.setAttributeValue(value);
    }
    
    
    public String getAttributeType() {
        return "characteristic[" + type + "]";
    }

    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add("Characteristic[" + type + "]");
        if (unit != null) {
            Collections.addAll(headersList, unit.headers());
        }
        if (termSourceREF != null) {
            headersList.add("Term Source Ref");
        }
        if (termSourceID != null) {
            headersList.add("Term Source ID");
        }
        String[] result = new String[headersList.size()];
        return headersList.toArray(result);
    }

    public String[] values() {
        List<String> valuesList = new ArrayList<String>();
        valuesList.add(getAttributeValue());
        if (unit != null) {
            Collections.addAll(valuesList, unit.values());
        }
        if (termSourceREF != null) {
            valuesList.add(termSourceREF);
        }
        if (termSourceID != null) {
            valuesList.add(termSourceID);
        }
        String[] result = new String[valuesList.size()];
        return valuesList.toArray(result);
    }
}