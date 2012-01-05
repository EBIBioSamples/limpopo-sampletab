package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute.SCDNodeAttribute;

public class SampleNode extends AbstractSCDNode {
	public String sampleDescription;
	public String sampleAccession; // TODO make getter/setter for this that
									// checks value
	private final List<SCDNodeAttribute> attributes = new ArrayList<SCDNodeAttribute>();

	public String[] headers() {
		List<String> headersList = new ArrayList<String>();
		headersList.add("Sample Name");
		if (sampleAccession != null) {
			headersList.add("Sample Accession");
		}
		if (sampleDescription != null) {
			headersList.add("Sample Description");
		}
		for (SCDNodeAttribute a : attributes) {
			Collections.addAll(headersList, a.headers());
		}
		String[] result = new String[headersList.size()];
		return headersList.toArray(result);
	}

	public String[] values() {
		List<String> valuesList = new ArrayList<String>();
		valuesList.add(nodeName);
		if (sampleAccession != null) {
			valuesList.add(sampleAccession);
		}
		if (sampleDescription != null) {
			valuesList.add(sampleDescription);
		}
		for (SCDNodeAttribute a : attributes) {
			Collections.addAll(valuesList, a.values());
		}
		String[] result = new String[valuesList.size()];
		return valuesList.toArray(result);
	}

	public String getNodeType() {
		return "sample";
	}

	public void addAttribute(SCDNodeAttribute attribute, int pos) {
		attributes.add(pos, attribute);
	}

	public void addAttribute(SCDNodeAttribute attribute) {
		attributes.add(attribute);
	}

	public List<SCDNodeAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * {@link #Node}  are determined to be equal() if they are of the same class, have
	 * the same {@link #getNodeType()} value and the same {@link #getNodeName()}
	 * value. SampleNodes have the additional restriction that their
	 * {@link #sampleAccession} should be equal as well.
	 * 
	 * @param obj
	 *            the object to compare for equality
	 * @return true if these objects are equal, false otherwise
	 */
    @Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		else if (other == this)
			return true;

		if (other instanceof SampleNode) {
			SampleNode that = (SampleNode) other;
			return new EqualsBuilder()
					.append(this.getNodeType(), that.getNodeType())
					.append(this.getNodeName(), that.getNodeName())
					.append(this.sampleAccession, that.sampleAccession)
					.isEquals();
		} else {
			return false;
		}
	}

    @Override
	public int hashCode() {
		return new HashCodeBuilder(11, 31)
				// two randomly chosen prime numbers
				.append(this.getNodeType()).append(this.getNodeName())
				.append(this.sampleAccession).toHashCode();
	}
}