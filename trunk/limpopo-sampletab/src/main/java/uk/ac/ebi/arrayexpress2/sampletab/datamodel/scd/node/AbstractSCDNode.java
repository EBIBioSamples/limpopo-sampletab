package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.AbstractNode;

public abstract class AbstractSCDNode extends AbstractNode implements SCDNode {
    public String toString() {
        StringBuilder hb = new StringBuilder();
        for (String header : headers()) {
            hb.append(header).append("\t");
        }
        String header = hb.toString().trim();

        StringBuilder vb = new StringBuilder();
        for (String value : values()) {
            vb.append(value).append("\t");
        }
        String value = vb.toString().trim();

        return header + System.getProperty("line.separator") + value;
    }
}
