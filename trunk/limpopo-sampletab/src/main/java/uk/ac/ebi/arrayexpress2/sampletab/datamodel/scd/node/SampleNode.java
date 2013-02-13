package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

/**
 * This represents a sample object.
 * 
 * Three attributes are built into this object "Sample Name, "Sample Accession" 
 * and "Sample Description". All other attributes should be added through an 
 * addAttribute method.
 * 
 * When samples are derived from each other, the addChildNode and addParentNode
 * methods should be used. Note that these are not reciprocal so both
 * must be called to function correctly.
 */
public class SampleNode extends AbstractSCDNode {

    private String sampleDescription = null;
	private String sampleAccession = null;

    public SampleNode() {
        super();
    }
    
    public SampleNode(String name) {
        super(name);
    }
	
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

	/**
	 * Returns the type of this node. This should be a lowercased 
	 * and space-removed version of the first column header so
	 * that parsing works.
	 */
	public String getNodeType() {
		return "samplename";
	}
	
	public void removeAttribute(SCDNodeAttribute attribute){
	    attributes.remove(attribute);
	}

	public List<SCDNodeAttribute> getAttributes() {
		return attributes;
	}

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        if (sampleDescription != null){
            sampleDescription = sampleDescription.trim();
            if (sampleDescription.length() == 0)
                sampleDescription = null;
        }
        this.sampleDescription = sampleDescription;
    }

    public String getSampleAccession() {
        return sampleAccession;
    }

    public void setSampleAccession(String sampleAccession) {
        if (sampleAccession != null){
            sampleAccession = sampleAccession.trim();
            if (sampleAccession.length() == 0) {
                sampleAccession = null;
            } else if (!sampleAccession.matches("SAM[EN]A?[0-9]+")){
                throw new IllegalArgumentException(sampleAccession+" is not a valid sample accession");
            }
        }
        this.sampleAccession = sampleAccession;
    }
}