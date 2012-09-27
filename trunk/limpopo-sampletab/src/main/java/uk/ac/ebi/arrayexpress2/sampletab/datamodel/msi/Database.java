package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Representation of an database cross-reference used in {@link SampleTab} {@link MSI}.
 * 
 * Is immutable, comparable, and hashable.
 * 
 * @author Adam Faulconbridge
 */
public class Database implements Comparable<Database> {
	private final String name;
	private final String id;
	private final String uri;

	/**
	 * Will accept null values and convert zero-length strings to null. This is needed 
	 * so that they can be built up line-by-line when reading from a file.
	 */
	public Database(String name, String uri, String id) {
        if (name == null || name.trim().length() == 0)
            this.name = null;
        else
            this.name = name.trim();
        
        if (uri == null || uri.trim().length() == 0)
            this.uri = null;
        else
            this.uri = uri.trim();
        
        if (id == null || id.trim().length() == 0)
            this.id = null;
        else
            this.id = id.trim();
	}

    public String getName() {
		return name;
	}

	public String getID() {
		return id;
	}

	public String getURI() {
		return uri;
	}

    @Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
        } else if (!this.getClass().isInstance(other)){
            return false;
        } else {
        	Database dother = (Database) other;
    		return new EqualsBuilder()
    			.append(this.getName(), dother.getName())
    			.append(this.getURI(), dother.getURI())
    			.append(this.getID(), dother.getID())
    			.isEquals();
            }
	}

	public int hashCode() {
		return new HashCodeBuilder(13, 31) // two prime numbers
			.append(this.getName())
			.append(this.getURI())
			.append(this.getID())
			.toHashCode();
	}

    public int compareTo(Database other) {
        if (other == null) {
            return -1;
        } else if (other == this) {
            return 0;
        } else {
            return new CompareToBuilder()
                .append(this.getName(), other.getName())
                .append(this.getURI(), other.getURI())
                .append(this.getID(), other.getID())
                .toComparison();
        }
    }

}
