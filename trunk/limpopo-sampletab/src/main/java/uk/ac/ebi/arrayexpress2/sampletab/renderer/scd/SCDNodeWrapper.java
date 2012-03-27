package uk.ac.ebi.arrayexpress2.sampletab.renderer.scd;

import org.isatools.tablib.export.graph2tab.DefaultAbstractNode;
import org.isatools.tablib.export.graph2tab.DefaultTabValueGroup;
//import org.isatools.tablib.export.graph2tab.Node;
import org.isatools.tablib.export.graph2tab.TabValueGroup;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import java.util.*;


public class SCDNodeWrapper extends DefaultAbstractNode {
    private SCDNode base;
    

    public SCDNodeWrapper(SCDNode base) {
        super();
        this.base = base;
    }


    public List<TabValueGroup> getTabValues() {
        String[] headers = base.headers();
        String[] values = base.values();
        if (headers.length == 0) {
            throw new RuntimeException(
                    "Internal error: got a node without headers/values!, node: " + base.getNodeName() + "/" +
                            base.getNodeType()
            );
        }

        List<TabValueGroup> result = new ArrayList<TabValueGroup>();
        for (int i = 0; i < headers.length; i++) {
            result.add(new DefaultTabValueGroup(headers[i], values[i]));
        }
        return result;
    }

    @Override
    public SortedSet<org.isatools.tablib.export.graph2tab.Node> getInputs() {
        if (inputs != null) {
            return super.getInputs();
        }
        inputs = new TreeSet<org.isatools.tablib.export.graph2tab.Node>();
        
        SCDNodeFactory nodeFact = SCDNodeFactory.getInstance();
        for (uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node in : base.getParentNodes()) {
        	if (in != null){
        	//if (in != null && in instanceof SCDNode ){
        		inputs.add(nodeFact.getNode((SCDNode) in));
        	}
        }
        
        return super.getInputs();
    }

    @Override
    public SortedSet<org.isatools.tablib.export.graph2tab.Node> getOutputs() {
        if (outputs != null) {
            return super.getOutputs();
        }
        outputs = new TreeSet<org.isatools.tablib.export.graph2tab.Node>();
        
        SCDNodeFactory nodeFact = SCDNodeFactory.getInstance();
        for (uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node out : base.getChildNodes()) {
        	if (out != null){
            //if (out != null && out instanceof SCDNode){
        		outputs.add(nodeFact.getNode((SCDNode) out));
        	}
        }
        
        return super.getOutputs();
    }
    
    @Override
    public int getOrder() {
        SCDNodeFactory nodeFact = SCDNodeFactory.getInstance();

		int parentOrder = 100;
		for (uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node parentNode : base.getParentNodes()){
			int thisParentOrder = nodeFact.getNode((SCDNode) parentNode).getOrder()+1;
			if (thisParentOrder > parentOrder){
				parentOrder = thisParentOrder;
			}	
		}
		return parentOrder;
    }
    
}
