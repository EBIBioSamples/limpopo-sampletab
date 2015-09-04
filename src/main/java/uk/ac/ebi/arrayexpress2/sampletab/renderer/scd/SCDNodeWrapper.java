package uk.ac.ebi.arrayexpress2.sampletab.renderer.scd;

import org.isatools.tablib.export.graph2tab.DefaultAbstractNode;
import org.isatools.tablib.export.graph2tab.DefaultTabValueGroup;
//import org.isatools.tablib.export.graph2tab.Node;
import org.isatools.tablib.export.graph2tab.TabValueGroup;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

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
        /*
        for (int i = 0; i < headers.length; i++) {
            result.add(new DefaultTabValueGroup(headers[i], values[i]));
        }
        return result;
        */
        
        //work out what headers are part of the node, and not any attributes
        List<String> headersList = new ArrayList<String>(base.headers().length);
        for(String header : base.headers()){
            headersList.add(header);
        }
        int firstAttrHeaderIndex = base.headers().length;
        if (base.getAttributes().size() > 0){
            String firstNonBase = base.getAttributes().get(0).headers()[0];
            firstAttrHeaderIndex = headersList.indexOf(firstNonBase);
            //System.out.println(">>> "+firstNonBase+" ("+firstAttrHeaderIndex+")");
        }

        //create a value group for the node itself
        DefaultTabValueGroup tail = null;
        DefaultTabValueGroup head = null;
        for (int i = firstAttrHeaderIndex-1; i >= 0; i--) {
            if (tail == null){
                head = new DefaultTabValueGroup(headers[i], values[i]);
            } else {
                head = new DefaultTabValueGroup(headers[i], values[i], tail);
            }
            tail = head;
        }
        
        DefaultTabValueGroup start = head;
        result.add(start);
        
        //now create value groups for each of the node attributes
        //e.g.
        /*
        * <pre>
        *   TabValueGroup ( 
        *     header = "Characteristics [ Type ]" 
        *     value = "value1"
        *     tail = ( 
        *       TabValueGroup (
        *         header = "Term Source REF"
        *         value = "source1"
        *         tail = (
        *           TabValueGroup (
        *             header = "Term Accession"
        *             value = "acc1"
        *           )
        *         )
        *       )
        *     )
        *   )
        * </pre>
        */
        for(SCDNodeAttribute attr : base.getAttributes()){
            headers = attr.headers();
            values = attr.values();
            head = null;
            tail = null;
            
            for (int i = headers.length-1; i >= 0; i--) {
                if (tail == null){
                    head = new DefaultTabValueGroup(headers[i], values[i]);
                } else {
                    head = new DefaultTabValueGroup(headers[i], values[i], tail);
                }
                tail = head;
            }
            if (head != null){
                start.append(head);
            }
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
