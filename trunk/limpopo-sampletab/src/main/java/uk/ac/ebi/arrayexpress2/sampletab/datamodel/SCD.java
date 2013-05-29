package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.AbstractGraph;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

/**
 * Sample Characteristic Description in a SampleTab File.
 * 
 * Read from a SampleTab file by {@Link uk.ac.ebi.arrayexpress2.sampletab.parser.SCDParser} and written out
 * by {@link uk.ac.ebi.arrayexpress2.renderer.SCDWriter}.
 */
public class SCD extends AbstractGraph<SCDNode> {

    //for internal use in error logs
    private URL location = null;
    
    private Logger log = LoggerFactory.getLogger(getClass());

    public void setLocation(URL location) {
        if (location != null ) {
            throw new IllegalArgumentException("Can only specify location once");
        }
        this.location = location;
    }
    
    public URL getLocation() {
        return location;
    }
}
