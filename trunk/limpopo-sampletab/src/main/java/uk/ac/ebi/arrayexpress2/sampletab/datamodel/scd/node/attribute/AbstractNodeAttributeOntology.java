package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public abstract class AbstractNodeAttributeOntology extends AbstractNodeAttribute {
    private String termSourceREF;
    private String termSourceID;
    
    public String getTermSourceREF() {
        return termSourceREF;
    }

    public void setTermSourceREF(String termSourceREF) {
        if (termSourceREF != null && termSourceREF.length() == 0){
            termSourceREF = null;
        }
        this.termSourceREF = termSourceREF;
    }

    public String getTermSourceID() {
        return termSourceID;
    }

    public void setTermSourceID(String termSourceID) {
        if (termSourceID != null && termSourceID.length() == 0){
            termSourceID = null;
        }
        this.termSourceID = termSourceID;
    }
    
    public void setTermSourceIDInteger(Integer termSourceID) {
        if (termSourceID == null){
            this.termSourceID = null;
        } else {
            this.termSourceID = termSourceID.toString();
        }
    }
}
