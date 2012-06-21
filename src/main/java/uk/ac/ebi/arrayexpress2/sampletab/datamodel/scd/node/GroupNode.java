package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

/**
 * This represents a group object.
 * 
 * Three attributes are built into this object "Group Name, "Group Accession" 
 * and "Group Description". All other attributes should be added through an 
 * addAttribute method. 
 * 
 * Samples can be added to the group by the addSample method. This calls the
 * corresponding addParentNode and addChildNode methods, which should not be
 * used otherwise.
 */
public class GroupNode extends AbstractSCDNode {
    private String groupDescription;
    private String groupAccession; //TODO make getter/setter for this that checks value

    public GroupNode(){
        super();
    }
    
    public GroupNode(String name){
        this();
    	this.setNodeName(name);    	
    }
    
	public void addSample(SampleNode sample){
		this.addParentNode(sample);
        sample.addChildNode(this);
	}
    
    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add("Group Name");
        if (groupAccession != null) {
            headersList.add("Group Accession");
        }
        if (groupDescription != null) {
            headersList.add("Group Description");
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
        if (groupAccession != null) {
        	valuesList.add(groupAccession);
        }
        if (groupDescription != null) {
        	valuesList.add(groupDescription);
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
        return "groupname";
    }

	public List<SCDNodeAttribute> getAttributes() {
		return attributes;
	}

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        if (groupDescription != null){
            groupDescription = groupDescription.trim();
            if (groupDescription.length() == 0)
                groupDescription = null;
        }
        this.groupDescription = groupDescription;
    }

    public String getGroupAccession() {
        return groupAccession;
    }

    public void setGroupAccession(String groupAccession) {
        if (groupAccession != null ){
            groupAccession = groupAccession.trim();
            if (groupAccession.length() == 0)
                groupAccession = null;
        }
        this.groupAccession = groupAccession;
    }
}