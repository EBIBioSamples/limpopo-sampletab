package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.isatools.tablib.export.graph2tab.TabValueGroup;
//import org.isatools.tablib.export.graph2tab.DefaultAbstractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.AbstractNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttributeComparator;



public abstract class AbstractSCDNode extends AbstractNode implements SCDNode {
    
    public final List<SCDNodeAttribute> attributes = new ArrayList<SCDNodeAttribute>();
	
    public String toString() {
        StringBuilder hb = new StringBuilder();
        for (String header : headers()) {
            hb.append(header).append("\t");
        }
        String header = hb.toString().trim();

        StringBuilder vb = new StringBuilder();
        for (String value : values()) {
            vb.append(value).append("\t");
        }
        String value = vb.toString().trim();

        return header + System.getProperty("line.separator") + value;
    }

    public void addAttribute(SCDNodeAttribute attribute) {
        addAttribute(attribute, attributes.size());
    }

    public void addAttribute(SCDNodeAttribute attribute, int pos) {
        //check it has a key
        if (attribute.getAttributeType() == null || attribute.getAttributeType().trim().length() == 0){
            return;
        }
        
        //check it has a value
        if (attribute.getAttributeValue() == null || attribute.getAttributeValue().trim().length() == 0){
            return;
        }
        //check it does not already exist
        Comparator<SCDNodeAttribute> c = new SCDNodeAttributeComparator();
        for (SCDNodeAttribute a : attributes) {
            if (c.compare(attribute, a) == 0 ) {
                return;
            }
        }
        
        attributes.add(pos, attribute);
    }

	public List<TabValueGroup> getTabValues() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}
