package uk.ac.ebi.arrayexpress2.sampletab.handler.scd.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.GroupNode;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.SCDReadHandler;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.CharacteristicAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.CommentAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.CountAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.DatabaseAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.MaterialAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.OrganismAttributeReader;
import uk.ac.ebi.arrayexpress2.sampletab.handler.scd.node.attribute.SexAttributeReader;

@ServiceProvider
public class SCDGroupNameHandler extends SCDReadHandler {
    private final CharacteristicAttributeReader characteristicAttributeReader;
    private final CommentAttributeReader commentAttributeReader;
    private final OrganismAttributeReader organismAttributeReader;
    private final SexAttributeReader sexAttributeReader;
    private final MaterialAttributeReader materialAttributeReader;
    private final DatabaseAttributeReader databaseAttributeReader;
    private final CountAttributeReader countAttributeReader;

    public SCDGroupNameHandler() {
        characteristicAttributeReader = new CharacteristicAttributeReader();
        commentAttributeReader = new CommentAttributeReader();
        organismAttributeReader = new OrganismAttributeReader();
        sexAttributeReader = new SexAttributeReader();
        materialAttributeReader = new MaterialAttributeReader();
        databaseAttributeReader = new DatabaseAttributeReader();
        countAttributeReader = new CountAttributeReader();
    }

    public boolean canReadHeader(String[] header) {
        return header[0].equals("groupname");
    }

    @Override
    public int assess(String[] header) {
        for (int i = 1; i < header.length;) {
        	if (header[i].equals("groupdescription")) {
                // don't need to do anything here
            }
        	else if (header[i].equals("groupaccession")) {
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
            else if (header[i].equals("count")) {
                i += assessAttribute(sexAttributeReader, header, i);
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
        GroupNode group;
        boolean newGroup = false; 

        // first row, so lookup or make a new source
        synchronized (scd) {
            group = scd.getNode(data[0], GroupNode.class);
            if (group == null) {
                newGroup = true;
                group = new GroupNode();
                group.setNodeName(data[0]);
                scd.addNode(group);
            }
        }
                
        // now do the rest if a new group was created
        if (newGroup) {
            for (int i = 1; i < data.length;) {
            	if (header[i].equals("groupdescription")) {
                    group.setGroupDescription(data[i]);
                }
            	else if (header[i].equals("groupaccession")) {
                    group.setGroupAccession(data[i]);
                }
                else if (header[i].equals("organism")) {
                    i += readAttribute(organismAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else if (header[i].equals("sex")) {
                    i += readAttribute(sexAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else if (header[i].equals("material")) {
                    i += readAttribute(materialAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else if (header[i].equals("databasename")) {
                    i += readAttribute(databaseAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else if (header[i].startsWith("characteristic")) {
                    i += readAttribute(characteristicAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else if (header[i].startsWith("comment")) {
                    i += readAttribute(commentAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else if (header[i].startsWith("count")) {
                    i += readAttribute(countAttributeReader, header, data, scd, group, lineNumber,
                            columnNumber + i, i);
                }
                else {
                    // got to something we don't recognise
                    // this is either the end, or a bad column name
                    // update the child node
                    updateChildNode(header, data, group, i);
                    break;
                }
                i++;
            }
        }

        // iterated over every column, so must have reached the end
        // update node in SDRF
        scd.updateNode(group);

    }
}