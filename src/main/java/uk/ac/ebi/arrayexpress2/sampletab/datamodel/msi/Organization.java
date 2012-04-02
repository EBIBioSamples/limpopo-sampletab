package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Representation of an organization as used in {@link SampleTab} {@link MSI}.
 * 
 * Is immutable, comparable, and hashable.
 * 
 * @author Adam Faulconbridge
 */
public class Organization {
	private final String name;
	private final String address;
	private final String uri;
	private final String email;
	private final String role;

    /**
     * Will accept null values and convert zero-length strings to null. This is needed 
     * so that they can be built up line-by-line when reading from a file.
     */
	public Organization(String name, String address, String uri, String email,
			String role) {
        if (name == null || name.trim().length() == 0)
            this.name = null;
        else
            this.name = name.trim();

        if (address == null || address.trim().length() == 0)
            this.address = null;
        else
            this.address = address.trim();

        if (uri == null || uri.trim().length() == 0)
            this.uri = null;
        else
            this.uri = uri.trim();

        if (email == null || email.trim().length() == 0)
            this.email = null;
        else
            this.email = email.trim();

        if (role == null || role.trim().length() == 0)
            this.role = null;
        else
            this.role = role.trim();
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getURI() {
		return uri;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
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
        	Organization oother = (Organization) other;
    		return new EqualsBuilder()
    			.append(this.getName(), oother.getName())
    			.append(this.getAddress(), oother.getAddress())
    			.append(this.getURI(), oother.getURI())
    			.append(this.getEmail(), oother.getEmail())
    			.append(this.getRole(), oother.getRole())
    			.isEquals();
        }
	}

	public int hashCode() {
		return new HashCodeBuilder(13, 31) // two randomly chosen prime numbers
			.append(this.getName())
			.append(this.getAddress())
			.append(this.getURI())
			.append(this.getEmail())
			.append(this.getRole())
			.toHashCode();
	}

}
