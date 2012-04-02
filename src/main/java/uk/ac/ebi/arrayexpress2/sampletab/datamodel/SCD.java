package uk.ac.ebi.arrayexpress2.sampletab.datamodel;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.AbstractGraph;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;

/**
 * Sample Characteristic Description in a SampleTab File.
 * 
 * Read from a SampleTab file by {@Link uk.ac.ebi.arrayexpress2.sampletab.parser.SCDParser} and written out
 * by {@link uk.ac.ebi.arrayexpress2.renderer.SCDWriter}.
 */
public class SCD extends AbstractGraph<SCDNode> {

}
