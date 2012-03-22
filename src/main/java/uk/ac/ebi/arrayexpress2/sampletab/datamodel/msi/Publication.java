package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Publication {
	private final String doi;
	private final String pubmedid;

	public Publication(String pubmedid, String doi) {
		this.doi = doi;
		this.pubmedid = pubmedid;
	}

	public String getDOI() {
		return doi;
	}

	public String getPubMedID() {
		return pubmedid;
	}

	public boolean equals(Publication other) {
		if (other == null)
			return false;
		else if (other == this)
			return true;

		return new EqualsBuilder()
			.append(this.getDOI(), other.getDOI())
			.append(this.getPubMedID(), other.getPubMedID())
			.isEquals();

	}

	public int hashCode() {
		return new HashCodeBuilder(13, 31) // two randomly chosen prime numbers
			.append(this.getDOI())
			.append(this.getPubMedID())
			.toHashCode();
	}

}
