package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node;
import uk.ac.ebi.arrayexpress2.sampletab.comparator.scd.node.attribute.SCDNodeAttributeComparator;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class ComparatorSCDNode implements Comparator<SCDNode> {
    
    private Comparator<List<String>> cl = new ComparatorList<String>();
    private SCDNodeAttributeComparator scdcomp = new SCDNodeAttributeComparator();
    private ComparatorListComparator<SCDNodeAttribute> cla = new ComparatorListComparator<SCDNodeAttribute>(scdcomp);
    private Comparator<SortedSet<SCDNode>> cs = new ComparatorSortedSet<SCDNode>();
    private Comparator<SortedSet<SCDNodeAttribute>> csa = new ComparatorSortedSet<SCDNodeAttribute>();
    
    private boolean ordered = false;
    
    public ComparatorSCDNode(boolean ordered) {
        this.ordered = ordered;
    }
    
    public int compare(SCDNode o1, SCDNode o2) {

        int score;
                
        score = o1.getNodeType().compareTo(o2.getNodeType());
        if (score != 0) {
            return score;
        }
        score = o1.getNodeName().compareTo(o2.getNodeName());
        if (score != 0) {
            return score;
        }
        
        //compare by attributes
        if (ordered) {
            score = cla.compare(o1.getAttributes(), o2.getAttributes());
            if (score != 0) {
                return score;
            }
        } else {
            SortedSet<SCDNodeAttribute> ssa1 = new TreeSet<SCDNodeAttribute>(scdcomp);
            ssa1.addAll(o1.getAttributes());
            SortedSet<SCDNodeAttribute> ssa2 = new TreeSet<SCDNodeAttribute>(scdcomp);
            ssa2.addAll(o2.getAttributes());
            score = csa.compare(ssa1, ssa2);
            if (score != 0) {
                return score;
            }
        }

        //compare the children
        SortedSet<SCDNode> sso1 = new TreeSet<SCDNode>(this);
        for (Node n : o1.getChildNodes()){
            //TODO dont do this
            sso1.add((SCDNode) n);
        }
        SortedSet<SCDNode> sso2 = new TreeSet<SCDNode>(this);
        for (Node n : o2.getChildNodes()){
            //TODO dont do this
            sso2.add((SCDNode) n);
        }

        score = cs.compare(sso1, sso2);        
        if (score != 0) {
            return score;
        }
        
        return 0;
        
    }

}
