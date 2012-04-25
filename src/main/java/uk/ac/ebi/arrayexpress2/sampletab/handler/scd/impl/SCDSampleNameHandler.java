package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.SCDReadHandler;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.CharacteristicAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.ChildOfAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.CommentAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.DatabaseAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.DerivedFromAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.MaterialAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.OrganismAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.SexAttributeReader;

@ServiceProvider
public class SCDSampleNameHandler extends SCDReadHandler {
    private final CharacteristicAttributeReader characteristicAttributeReader;
    private final CommentAttributeReader commentAttributeReader;
    private final OrganismAttributeReader organismAttributeReader;
    private final SexAttributeReader sexAttributeReader;
    private final MaterialAttributeReader materialAttributeReader;
    private final DatabaseAttributeReader databaseAttributeReader;
    private final ChildOfAttributeReader childOfAttributeReader;
    private final DerivedFromAttributeReader derivedFromAttributeReader;

    public SCDSampleNameHandler() {
        characteristicAttributeReader = new CharacteristicAttributeReader();
        commentAttributeReader = new CommentAttributeReader();
        organismAttributeReader = new OrganismAttributeReader();
        sexAttributeReader = new SexAttributeReader();
        materialAttributeReader = new MaterialAttributeReader();
        databaseAttributeReader = new DatabaseAttributeReader();
        childOfAttributeReader = new ChildOfAttributeReader();
        derivedFromAttributeReader = new DerivedFromAttributeReader();
    }

    public boolean canReadHeader(String[] header) {
        return header[0].equals("samplename");
    }

    @Override
    public int assess(String[] header) {
        for (int i = 1; i < header.length;) {
        	if (header[i].equals("sampledescription")) {
                // don't need to do anything here
            }
        	else if (header[i].equals("sampleaccession")) {
                // don't need to do anything here
            }
            else if (header[i].equals("organism")) {
                i += assessAttribute(organismAttributeReader, header, i);
            }
            else if (header[i].equals("sex")) {
                i += assessAttribute(sexAttributeReader, header, i);
            }
            else if (header[i].equals("material")) {
                i += assessAttribute(materialAttributeReader, header, i);
            }
            else if (header[i].equals("databasename")) {
                i += assessAttribute(databaseAttributeReader, header, i);
            }
            else if (header[i].startsWith("characteristic")) {
                i += assessAttribute(characteristicAttributeReader, header, i);
            }
            else if (header[i].startsWith("comment")) {
                i += assessAttribute(commentAttributeReader, header, i);
            }
            else if (header[i].startsWith("childof")) {
                i += assessAttribute(childOfAttributeReader, header, i);
            }
            else if (header[i].startsWith("derivedfrom")) {
                i += assessAttribute(derivedFromAttributeReader, header, i);
            }
            else {
                // got to something we don't recognise
                // this is either the end, or a bad column name
                return i;
            }
            i++;
        }

        // iterated over every column, so must have reached the end
        return header.length;
    }

    public void readData(String[] header, String[] data, SCD scd, int lineNumber, int columnNumber)
            throws ParseException {
        // SampleNode to create/modify
        SampleNode sample;

        // first row, so lookup or make a new source
        synchronized (scd) {
            sample = scd.getNode(data[0], SampleNode.class);
            if (sample == null) {
                sample = new SampleNode();
                sample.setNodeName(data[0]);
                scd.addNode(sample);
            }
        }
                
        // now do the rest
        for (int i = 1; i < data.length;) {
        	if (header[i].equals("sampledescription")) {
                sample.setSampleDescription(data[i]);
            }
        	else if (header[i].equals("sampleaccession")) {
                sample.setSampleAccession(data[i]);
            }
            else if (header[i].equals("organism")) {
                i += readAttribute(organismAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].equals("sex")) {
                i += readAttribute(sexAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].equals("material")) {
                i += readAttribute(materialAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].equals("databasename")) {
                i += readAttribute(databaseAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].startsWith("characteristic")) {
                i += readAttribute(characteristicAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].startsWith("comment")) {
                i += readAttribute(commentAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].startsWith("childof")) {
                i += readAttribute(childOfAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else {
                // got to something we don't recognise
                // this is either the end, or a bad column name
                // update the child node
                updateChildNode(header, data, sample, i);
                break;
            }
            i++;
        }

        // iterated over every column, so must have reached the end
        // update node in SDRF
        scd.updateNode(sample);

    }
}