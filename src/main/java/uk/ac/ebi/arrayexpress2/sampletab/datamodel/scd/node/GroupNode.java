package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class GroupNode extends AbstractSCDNode {
    public String groupDescription;
    public String groupAccession; //TODO make getter/setter for this that checks value
    public final List<SCDNodeAttribute> attributes = new ArrayList<SCDNodeAttribute>();

    public GroupNode(){
        super();
    }
    
    public GroupNode(String name){
        this();
    	this.setNodeName(name);    	
    }
    
	public void addSample(SCDNode sample){
	    //TODO verify it is a sample not another group
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
    
    public String getNodeType() {
        return "groupname";
    }

	public void addAttribute(SCDNodeAttribute attribute) {
		attributes.add(attribute);
	}

	public List<SCDNodeAttribute> getAttributes() {
		return attributes;
	}
}