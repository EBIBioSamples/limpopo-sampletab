package uk.ac.ebi.arrayexpress2.sampletab.renderer.scd;

import org.isatools.tablib.export.graph2tab.Node;
import org.isatools.tablib.export.graph2tab.TableBuilder;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

import java.util.Collection;
import java.util.HashSet;

public class SCDTableBuilder extends TableBuilder {
    public SCDTableBuilder(Collection<? extends SCDNode> nodes) {
        super();
        this.nodes = new HashSet<Node>();
        SCDNodeFactory nodeFact = SCDNodeFactory.getInstance();
        //wrappers are created here by getNode method
        for (SCDNode node : nodes) {
            this.nodes.add(nodeFact.getNode(node));
        }
    }
}
