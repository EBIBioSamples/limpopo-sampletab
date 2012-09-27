package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.Node;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

public class ComparatorSCDNode implements Comparator<SCDNode> {

    
    private Comparator<List<String>> cl = new ComparatorList<String>();
    private Comparator<SortedSet<SCDNode>> cs = new ComparatorSortedSet<SCDNode>();
    
    public int compare(SCDNode o1, SCDNode o2) {
        List<String> headers1 = new ArrayList<String>(Arrays.asList(o1.headers()));
        List<String> headers2 = new ArrayList<String>(Arrays.asList(o2.headers()));
        
        int score;
        score = cl.compare(headers1, headers2);        
        if (score != 0) {
            return score;
        }
        
        List<String> values1 = new ArrayList<String>(Arrays.asList(o1.values()));
        List<String> values2 = new ArrayList<String>(Arrays.asList(o2.values()));       

        score = cl.compare(values1, values2);        
        if (score != 0) {
            return score;
        }

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
