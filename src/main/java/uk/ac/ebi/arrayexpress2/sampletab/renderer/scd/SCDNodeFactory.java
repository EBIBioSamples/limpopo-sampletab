package uk.ac.ebi.arrayexpress2.sampletab.renderer.scd;

import org.isatools.tablib.export.graph2tab.AbstractNodeFactory;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

public class SCDNodeFactory extends AbstractNodeFactory<SCDNodeWrapper, SCDNode> {
    private SCDNodeFactory() {
    }

    private static final SCDNodeFactory instance = new SCDNodeFactory();

    public static SCDNodeFactory getInstance() {
        return instance;
    }

    @Override
    protected SCDNodeWrapper createNewNode(SCDNode base) {
        return new SCDNodeWrapper(base);
    }
}
