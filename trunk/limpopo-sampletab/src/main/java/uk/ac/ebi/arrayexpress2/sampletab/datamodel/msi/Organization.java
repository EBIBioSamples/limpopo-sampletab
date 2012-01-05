package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Organization {
	private final String name;
	private final String address;
	private final String uri;
	private final String email;
	private final String role;

	public Organization(String name, String address, String uri, String email,
			String role) {
		this.name = name;
		this.address = address;
		this.uri = uri;
		this.email = email;
		this.role = role;
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

	public boolean equals(Organization other) {
		if (other == null)
			return false;
		else if (other == this)
			return true;

		return new EqualsBuilder()
			.append(this.getName(), other.getName())
			.append(this.getAddress(), other.getAddress())
			.append(this.getURI(), other.getURI())
			.append(this.getEmail(), other.getEmail())
			.append(this.getRole(), other.getRole())
			.isEquals();

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
