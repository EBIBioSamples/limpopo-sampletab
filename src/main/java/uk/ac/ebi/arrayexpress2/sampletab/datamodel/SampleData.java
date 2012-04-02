package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

/**
 * Root class of the SampleTab representation and holds together a pair of MSI and SCD objects.
 */
public class SampleData {
    public final MSI msi;
    public final SCD scd;

    /**
     * Default constructor for starting from an empty model.
     * 
     * To read a file, see {@link uk.ac.ebi.arrayexpress2.sampletab.parser.SampleTabParser}.
     * 
     * To write a file, see {@link uk.ac.ebi.arrayexpress2.sampletab.renderer.SampleTabWriter}.
     */
    public SampleData() {
        msi = new MSI();
        scd = new SCD();
    }
}
