package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class SampleNode extends AbstractSCDNode {
    public String sampleDescription;
    public String sampleAccession; //TODO make getter/setter for this that checks value
    public final List<SCDNodeAttribute> attributes = new ArrayList<SCDNodeAttribute>();

    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add("Sample Name");
        if (sampleAccession != null) {
            headersList.add("Sample Accession");
        }
        if (sampleDescription != null) {
            headersList.add("Sample Description");
        }
        for (SCDNodeAttribute a : attributes) {
            Collections.addAll(headersList, a.headers());
        }
        String[] result = new String[headersList.size()];
        return headersList.toArray(result);
    }

    public String[] values() {
        List<String> valuesList = new ArrayList<String>();
        valuesList.add(nodeName);
        if (sampleAccession != null) {
        	valuesList.add(sampleAccession);
        }
        if (sampleDescription != null) {
        	valuesList.add(sampleDescription);
        }
        for (SCDNodeAttribute a : attributes) {
            Collections.addAll(valuesList, a.values());
        }
        String[] result = new String[valuesList.size()];
        return valuesList.toArray(result);
    }
    
    public String getNodeType() {
        return "sample";
    }

	public void addAttribute(SCDNodeAttribute attribute) {
		attributes.add(attribute);
	}

	public List<SCDNodeAttribute> getAttributes() {
		return attributes;
	}
}