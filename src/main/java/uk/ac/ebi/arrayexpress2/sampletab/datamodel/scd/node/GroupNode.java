package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class GroupNode extends AbstractSCDNode {
    public String groupDescription;
    public String grouupAccession; //TODO make getter/setter for this that checks value
    public final List<SCDNodeAttribute> attributes = new ArrayList<SCDNodeAttribute>();

    public GroupNode(){
        super();
    }
    
    public GroupNode(String name){
        this();
    	this.setNodeName(name);    	
    }
    
	public void addSample(SampleNode sample){
		addParentNode(sample);
	}
    
    public String[] headers() {
        List<String> headersList = new ArrayList<String>();
        headersList.add("Group Name");
        if (grouupAccession != null) {
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
        if (grouupAccession != null) {
        	valuesList.add(grouupAccession);
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
        return "group";
    }

	public void addAttribute(SCDNodeAttribute attribute) {
		attributes.add(attribute);
	}

	public List<SCDNodeAttribute> getAttributes() {
		return attributes;
	}
}