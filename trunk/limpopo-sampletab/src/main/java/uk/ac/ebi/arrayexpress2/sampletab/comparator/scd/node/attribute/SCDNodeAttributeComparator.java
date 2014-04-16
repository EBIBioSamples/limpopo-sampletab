package uk.ac.ebi.arrayexpress2.sampletab.comparator.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttributeOntology;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.DatabaseAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class SCDNodeAttributeComparator implements java.util.Comparator<SCDNodeAttribute> {

    public int compare(SCDNodeAttribute arg0, SCDNodeAttribute arg1) {
        //handle nulls
        if (arg0 == null) {
            if (arg1 == null) {
                return 0;
            } else { 
                return 1;
            }
        } else if (arg1 == null) {
            return -1;
        }
        
        //compare based on the headers and values of the attribute
        if (arg0.headers().length < arg0.headers().length) {
            return -1;
        } else if (arg0.headers().length > arg0.headers().length) {
            return 1;
        } else {
            for (int i = 0; i < arg0.headers().length; i++) {
                int j = arg0.headers()[i].compareTo(arg1.headers()[i]);
                if (j != 0) return j;
            }
            for (int i = 0; i < arg0.values().length; i++) {
                int j = arg0.values()[i].compareTo(arg1.values()[i]);
                if (j != 0) return j;
            }
            return 0;
        }

    }

}
