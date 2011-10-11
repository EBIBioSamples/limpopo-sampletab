package uk.ac.ebi.arrayexpress2.sampletab.datamodel;



public class SampleData {
    public final MSI msi;
    public final SCD scd;

    public SampleData() {
        msi = new MSI();
        scd = new SCD();
    }
}
