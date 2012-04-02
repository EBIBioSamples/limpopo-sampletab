package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Representation of an publication as used in {@link SampleTab} {@link MSI}.
 * 
 * Is immutable, comparable, and hashable.
 * 
 * @author Adam Faulconbridge
 */
public class Publication {
	private final String doi;
	private final String pubmedid;

    /**
     * Will accept null values and convert zero-length strings to null. This is needed 
     * so that they can be built up line-by-line when reading from a file.
     */
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
