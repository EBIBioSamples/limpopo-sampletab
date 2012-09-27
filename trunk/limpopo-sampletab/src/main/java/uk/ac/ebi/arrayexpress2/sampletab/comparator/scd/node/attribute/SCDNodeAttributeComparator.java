package uk.ac.ebi.arrayexpress2.sampletab.comparator.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNodeAttributeOntology;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class SCDNodeAttributeComparator implements java.util.Comparator<SCDNodeAttribute> {

    public int compare(SCDNodeAttribute arg0, SCDNodeAttribute arg1) {
        //handle nulls
        if (arg0 == null){
            if (arg1 == null){
                return 0;
            } else { 
                return 1;
            }
        } else if (arg1 == null){
            return -1;
        }
        
        if (arg0.getAttributeType().equals(arg1.getAttributeType())){
            if (arg0.getAttributeValue().equals(arg1.getAttributeValue())){
                //at his point, one or both of them may be ontology terms.
                //cast and behave accordingly
                
                boolean hasOntology = false;
                synchronized(AbstractNodeAttributeOntology.class){
                    if (AbstractNodeAttributeOntology.class.isInstance(arg0) && AbstractNodeAttributeOntology.class.isInstance(arg0)){
                        hasOntology = true;
                    }
                }
                
                if (hasOntology){
                    AbstractNodeAttributeOntology ont0 = (AbstractNodeAttributeOntology) arg0;
                    AbstractNodeAttributeOntology ont1 = (AbstractNodeAttributeOntology) arg1;
                
                    if (ont0.getTermSourceREF() == null && ont1.getTermSourceREF() == null) {
                        return 0;
                    } else if (ont0.getTermSourceREF() == null && ont1.getTermSourceREF() != null) {
                        return 1;
                    } else if (ont0.getTermSourceREF() != null && ont1.getTermSourceREF() == null) {
                        return -1;
                    }  else if (ont0.getTermSourceREF().equals(ont1.getTermSourceREF())) {
                        //fall back to id
                        if (ont0.getTermSourceID() == null && ont1.getTermSourceID() == null) {
                            return 0;
                        } else if (ont0.getTermSourceID() == null && ont1.getTermSourceID() != null) {
                            return 1;
                        } else if (ont0.getTermSourceID() != null && ont1.getTermSourceID() == null) {
                            return -1;
                        }  else if (ont0.getTermSourceID().equals(ont1.getTermSourceID())) {
                            return 0;
                            //new things to check go here...
                        } else {
                            return ont0.getTermSourceID().compareTo(ont1.getTermSourceID());
                        }
                    } else {
                        return ont0.getTermSourceREF().compareTo(ont1.getTermSourceREF());
                    }
                    
                } else {
                    return 0;
                }
            } else {
                return arg0.getAttributeValue().compareTo(arg1.getAttributeValue());
            }
        } else {
            return arg0.getAttributeType().compareTo(arg1.getAttributeType());
        }
        
    }

}
