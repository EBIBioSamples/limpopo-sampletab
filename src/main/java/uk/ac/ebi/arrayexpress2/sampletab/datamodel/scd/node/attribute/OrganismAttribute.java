package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.AbstractNamedAttribute ;

public class OrganismAttribute extends AbstractNamedAttribute {

    private static String name = "Organism";
    
    public OrganismAttribute(){
        super();
    }
    
    public OrganismAttribute(String taxname, String termsourceref, Integer termsourceid) {
        super(taxname);
        this.setTermSourceREF(termsourceref);
        this.setTermSourceID(termsourceid.toString());
    }

    public OrganismAttribute(String taxname) {
        super(taxname);
    }

    //need to keep this getter because value being get has been overriden
    public String getAttributeType() {
        return name;
    }
}
