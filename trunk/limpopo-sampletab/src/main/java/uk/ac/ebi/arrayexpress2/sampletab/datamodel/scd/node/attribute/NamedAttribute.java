package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import java.util.ArrayList;
import java.util.List;

public abstract class NamedAttribute extends AbstractNodeAttribute {
	protected String termSourceREF;
	protected String termSourceID;
    
    public String getTermSourceREF() {
		return termSourceREF;
	}

	public void setTermSourceREF(String termSourceREF) {
		this.termSourceREF = termSourceREF;
	}

	public String getTermSourceID() {
		return termSourceID;
	}

	public void setTermSourceID(String termSourceID) {
		this.termSourceID = termSourceID;
	}
	public void setTermSourceID(Integer termSourceID) {
		this.termSourceID = termSourceID.toString();
	}

	protected String name;

    public String getAttributeType() {
        return name;
    }

    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add(getAttributeType());
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