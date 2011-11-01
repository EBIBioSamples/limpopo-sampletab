package uk.ac.ebi.arrayexpress2.sampletab.renderer.scd;

import org.isatools.tablib.export.graph2tab.AbstractNodeFactory;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

public class NodeFactory extends AbstractNodeFactory<SCDNodeWrapper, SCDNode> {
    private NodeFactory() {
    }

    private static final NodeFactory instance = new NodeFactory();

    public static NodeFactory getInstance() {
        return instance;
    }

    @Override
    protected SCDNodeWrapper createNewNode(SCDNode base) {
        return new SCDNodeWrapper(base);
    }
}
