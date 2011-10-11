package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public interface SCDNodeAttribute {
    /**
     * Get the type of this attribute.  The attribute is typed (like Nodes) by the standardised "Name" tag for the node,
     * including any option qualifiers (such as contents of square brackets, parentheses, or prefixes in some cases).
     *
     * @return the attribute type, including qualifiers
     */
    String getAttributeType();

    /**
     * Get the value associated to this attribute in the spreadsheet.  In other words, the particular entry in the
     * current row under the given type.
     *
     * @return the attribute value
     */
    String getAttributeValue();


    /**
     * Returns a string array representing the headers of this node and all it's attributes.
     *
     * @return the headers unique to this node
     */
    String[] headers();

    /**
     * Returns the strings representing the values of this node and all it's attributes, indexed by the matching
     * headers. So, {@link #headers()} and <code>values</code> have exactly the same length and the same order.
     *
     * @return the values for this node
     */
    String[] values();
}
