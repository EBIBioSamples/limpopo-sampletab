package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node;

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
}
