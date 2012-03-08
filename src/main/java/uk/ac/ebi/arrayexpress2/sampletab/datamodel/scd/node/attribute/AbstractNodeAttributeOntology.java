package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public abstract class AbstractNodeAttributeOntology extends AbstractNodeAttribute {
    private String termSourceREF;
    private String termSourceID;
    
    public String getTermSourceREF() {
        return termSourceREF;
    }

    public void setTermSourceREF(String termSourceREF) {
        this.termSourceREF = termSourceREF;
    }

    public String getTermSourceID() {
        return termSourceID;
    }

    public void setTermSourceID(String termSourceID) {
        this.termSourceID = termSourceID;
    }
    
    public void setTermSourceID(Integer termSourceID) {
        this.termSourceID = termSourceID.toString();
    }
}
