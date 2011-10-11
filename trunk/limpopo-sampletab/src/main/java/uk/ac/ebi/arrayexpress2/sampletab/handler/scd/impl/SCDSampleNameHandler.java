package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.SCDReadHandler;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.CharacteristicAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.NamedAttributeReader;

@ServiceProvider
public class SCDSampleNameHandler extends SCDReadHandler {
    private final CharacteristicAttributeReader characteristicAttributeReader;
    private final NamedAttributeReader namedAttributeReader;

    public SCDSampleNameHandler() {
        characteristicAttributeReader = new CharacteristicAttributeReader();
        namedAttributeReader = new NamedAttributeReader();
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
                i += assessAttribute(namedAttributeReader, header, i);
            }
            else if (header[i].equals("sex")) {
                i += assessAttribute(namedAttributeReader, header, i);
            }
            else if (header[i].startsWith("characteristic")) {
                i += assessAttribute(characteristicAttributeReader, header, i);
            }
            else if (header[i].startsWith("comment")) {
                // don't need to do anything here
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
                sample.sampleDescription = data[i];
            }
        	else if (header[i].equals("sampleaccession")) {
                sample.sampleAccession = data[i];
            }
            else if (header[i].equals("organism")) {
                i += readAttribute(namedAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].equals("sex")) {
                i += readAttribute(namedAttributeReader, header, data, scd, sample, lineNumber,
                        columnNumber + i, i);
            }
            else if (header[i].startsWith("characteristics")) {
                i += readAttribute(characteristicAttributeReader, header, data, scd, sample, lineNumber,
                                   columnNumber + i, i);
            }
            else if (header[i].startsWith("comment")) {
            	
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