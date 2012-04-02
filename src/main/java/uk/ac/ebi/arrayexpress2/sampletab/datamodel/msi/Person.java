package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Representation of an person as used in {@link SampleTab} {@link MSI}.
 * 
 * Is immutable, comparable, and hashable.
 * 
 * @author Adam Faulconbridge
 */
public class Person {
	private final String lastname;
	private final String initials;
	private final String firstname;
	private final String email;
	private final String role;

    /**
     * Will accept null values and convert zero-length strings to null. This is needed 
     * so that they can be built up line-by-line when reading from a file.
     */
	public Person(String lastname, String initials, String firstname, String email,
			String role) {
        if (lastname == null || lastname.trim().length() == 0)
            this.lastname = null;
        else
            this.lastname = lastname.trim();
        
        if (initials == null || initials.trim().length() == 0)
            this.initials = null;
        else
            this.initials = initials.trim();
        
        if (firstname == null || firstname.trim().length() == 0)
            this.firstname = null;
        else
            this.firstname = firstname.trim();
        
        if (email == null || email.trim().length() == 0)
            this.email = null;
        else
            this.email = email.trim();
        
        if (role == null || role.trim().length() == 0)
            this.role = null;
        else
            this.role = role.trim();
	}

	public String getLastName() {
		return lastname;
	}

	public String getInitials() {
		return initials;
	}

	public String getFirstName() {
		return firstname;
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
        } else if (!this.getClass().isInstance(other)) {
            return false;
        } else {
        	Person pother = (Person) other;
    		return new EqualsBuilder()
    			.append(this.getFirstName(), pother.getFirstName())
    			.append(this.getInitials(), pother.getInitials())
    			.append(this.getLastName(), pother.getLastName())
    			.append(this.getEmail(), pother.getEmail())
    			.append(this.getRole(), pother.getRole())
    			.isEquals();
        }
	}

	public int hashCode() {
		return new HashCodeBuilder(7, 83) // two randomly chosen prime numbers
			.append(this.getFirstName())
			.append(this.getInitials())
			.append(this.getLastName())
			.append(this.getEmail())
			.append(this.getRole())
			.toHashCode();
	}

}
