package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Person {
	private final String lastname;
	private final String initials;
	private final String firstname;
	private final String email;
	private final String role;

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

	public boolean equals(Person other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
        } else if (!this.getClass().isInstance(other)) {
            return false;
        } else {
    
    		return new EqualsBuilder()
    			.append(this.getFirstName(), other.getFirstName())
    			.append(this.getInitials(), other.getInitials())
    			.append(this.getLastName(), other.getLastName())
    			.append(this.getEmail(), other.getEmail())
    			.append(this.getRole(), other.getRole())
    			.isEquals();
        }
	}

	public int hashCode() {
		return new HashCodeBuilder(13, 31) // two randomly chosen prime numbers
			.append(this.getFirstName())
			.append(this.getInitials())
			.append(this.getLastName())
			.append(this.getEmail())
			.append(this.getRole())
			.toHashCode();
	}

}