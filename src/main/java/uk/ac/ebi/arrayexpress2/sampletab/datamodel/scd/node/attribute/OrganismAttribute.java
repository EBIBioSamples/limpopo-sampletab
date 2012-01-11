package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

public class OrganismAttribute extends NamedAttribute {

    public String name = "Organism";
    
    public OrganismAttribute(){
        super();
    }
    
    public OrganismAttribute(String taxname, String termsourceref, Integer termsourceid) {
        super();
        this.setAttributeValue(taxname);
        this.setTermSourceREF(termsourceref);
        this.setTermSourceID(termsourceid);
    }

    public OrganismAttribute(String taxname) {
        super();
        this.setAttributeValue(taxname);
    }

    @Override
    public String getAttributeType() {
        return "Organism";
    }
}
