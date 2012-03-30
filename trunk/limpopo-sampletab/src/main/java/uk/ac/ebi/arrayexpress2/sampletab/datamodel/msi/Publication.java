package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Publication {
	private final String doi;
	private final String pubmedid;

	public Publication(String pubmedid, String doi) {
        if (doi == null || doi.trim().length() == 0)
            this.doi = null;
        else
            this.doi = doi.trim();

        if (pubmedid == null || pubmedid.trim().length() == 0)
            this.pubmedid = null;
        else
            //TODO cut to integer only
            this.pubmedid = pubmedid.trim();
	}

	public String getDOI() {
		return doi;
	}

	public String getPubMedID() {
		return pubmedid;
	}

    @Override
	public boolean equals(Object other) {
		if (other == null){
			return false;
		} else if (other == this) {
			return true;
        } else if (!this.getClass().isInstance(other)){
            return false;
		} else {
        	Publication pother = (Publication) other;
			return new EqualsBuilder()
				.append(this.getDOI(), pother.getDOI())
				.append(this.getPubMedID(), pother.getPubMedID())
				.isEquals();
		}
	}

	public int hashCode() {
		return new HashCodeBuilder(13, 31) // two randomly chosen prime numbers
			.append(this.getDOI())
			.append(this.getPubMedID())
			.toHashCode();
	}

}
