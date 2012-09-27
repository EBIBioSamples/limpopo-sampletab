package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class ComparatorSampleData implements Comparator<SampleData> {

    private Comparator<SCD> comparatorscd = new ComparatorSCD();

    private Logger log = LoggerFactory.getLogger(getClass());
    
    public int compare(SampleData arg1, SampleData arg2) {
        int score;
        
        log.info("comparing MSIs");
        
        score = arg1.msi.compareTo(arg2.msi);
        if (score != 0) return score;
        
        log.info("MSI is the same");
        
        log.info("comparing SCDs");
        
        score = comparatorscd.compare(arg1.scd, arg2.scd);
        if (score != 0) return score;
        

        log.info("SCD is the same");

        
        return 0;
    }
}
