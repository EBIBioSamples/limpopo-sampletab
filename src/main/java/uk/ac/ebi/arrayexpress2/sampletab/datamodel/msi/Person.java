package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Person {
	private final String lastname;
	private final String initials;
	private final String firstname;
	private final String email;
	private final String role;

	public Person(String lastname, String initials, String firstname, String uri, String email,
			String role) {
		this.lastname = lastname;
		this.initials = initials;
		this.firstname = firstname;
		this.email = email;
		this.role = role;
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
		if (other == null)
			return false;
		else if (other == this)
			return true;

		return new EqualsBuilder()
			.append(this.getFirstName(), other.getFirstName())
			.append(this.getInitials(), other.getInitials())
			.append(this.getLastName(), other.getLastName())
			.append(this.getEmail(), other.getEmail())
			.append(this.getRole(), other.getRole())
			.isEquals();

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
