package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.List;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node;
//import org.isatools.tablib.export.graph2tab.Node;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public interface SCDNode extends Node {
    /**
     * Returns a string array representing the headers of this node and all it's attributes.
     *
     * @return the headers unique to this node
     */
    String[] headers();

    /**
     * Returns the strings representing the values of this node and all it's attributes, indexed by the matching
     * headers. So, {@link #headers()} and {@link #values()} have exactly the same length and the same order.
     *
     * @return the values for this node
     */
    String[] values();

    void addAttribute(SCDNodeAttribute attribute);
    
    List<SCDNodeAttribute> getAttributes();
}
