package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Organization;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

public class ComparatorSCD implements Comparator<SCD> {

    private Comparator<SCDNode> comparatorscdnode = new ComparatorSCDNode();
    
    public int compare(SCD arg1, SCD arg2) {
        
        SortedSet<SCDNode> sso1 = new TreeSet<SCDNode>(comparatorscdnode);
        sso1.addAll(arg1.getRootNodes());
        
        SortedSet<SCDNode> sso2 = new TreeSet<SCDNode>(comparatorscdnode);
        sso2.addAll(arg2.getRootNodes());
        
        ComparatorSortedSet<SCDNode> c = new ComparatorSortedSet<SCDNode>();
        return c.compare(sso1, sso2);
    }

}
