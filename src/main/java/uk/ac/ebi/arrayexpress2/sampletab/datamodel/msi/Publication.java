package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of an publication as used in {@link SampleTab} {@link MSI}.
 * 
 * Is immutable, comparable, and hashable.
 * 
 * @author Adam Faulconbridge
 */
public class Publication implements Comparable<Publication>{
	private final String doi;
	private final String pubmedid;

    // logging
    private Logger log = LoggerFactory.getLogger(getClass());
    
    /**
     * Will accept null values and convert zero-length strings to null. This is needed 
     * so that they can be built up line-by-line when reading from a file.
     */
	public Publication(String pubmedid, String doi) {
        if (doi == null || doi.trim().length() == 0) {
            this.doi = null;
        } else { 
            this.doi = doi.trim();
        }

        if (pubmedid == null || pubmedid.trim().length() == 0) {
            
            this.pubmedid = null;
        } else {
            //cut down to integers only
            Integer number = null;
            try {
                number = Integer.parseInt(pubmedid);
            } catch (NumberFormatException e) {
                log.warn("Non-numeric pubmedid "+pubmedid);
            }
            if (number == null) {
                this.pubmedid = null;
            } else {
                this.pubmedid = number.toString();
            }
        }
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

    public int compareTo(Publication other) {
        if (other == null) {
            return -1;
        } else if (other == this) {
            return 0;
        } else {
            return new CompareToBuilder()
                .append(this.getDOI(), other.getDOI())
                .append(this.getPubMedID(), other.getPubMedID())
                .toComparison();
        }
    }

}
