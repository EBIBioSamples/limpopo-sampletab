package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAttribute extends AbstractNodeAttribute {
    public String databaseID;
    public String databaseURI;

    public DatabaseAttribute() {
        super();
    }

    public DatabaseAttribute(String name, String id, String uri) {
        this();
        this.setAttributeValue(name.trim());
        this.databaseID = id.trim();
        this.databaseURI = uri.trim();
    }

    public String getAttributeType() {
        return "Database Name";
    }

    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add(getAttributeType());
        if (databaseID != null) {
            headersList.add("Database ID");
        }
        if (databaseURI != null) {
            headersList.add("Database URI");
        }
        String[] result = new String[headersList.size()];
        return headersList.toArray(result);
    }

    public String[] values() {
        List<String> valuesList = new ArrayList<String>();
        valuesList.add(getAttributeValue());
        if (databaseID != null) {
            valuesList.add(databaseID);
        }
        if (databaseURI != null) {
            valuesList.add(databaseURI);
        }
        String[] result = new String[valuesList.size()];
        return valuesList.toArray(result);
    }
}
