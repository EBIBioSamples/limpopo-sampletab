package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Database {
	private final String name;
	private final String id;
	private final String uri;

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

	public boolean equals(Database other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
        } else if (!this.getClass().isInstance(other)){
            return false;
        } else {
    
    		return new EqualsBuilder()
    			.append(this.getName(), other.getName())
    			.append(this.getURI(), other.getURI())
    			.append(this.getID(), other.getID())
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

}
