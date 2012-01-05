package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TermSource {
	private final String name;
	private final String uri;
	private final String version;

	public TermSource(String name, String uri, String version) {
		this.name = name;
		this.uri = uri;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getURI() {
		return uri;
	}

	public String getVersion() {
		return version;
	}

	public boolean equals(TermSource other) {
		if (other == null)
			return false;
		else if (other == this)
			return true;

		return new EqualsBuilder()
			.append(this.getName(), other.getName())
			.append(this.getURI(), other.getURI())
			.append(this.getVersion(), other.getVersion())
			.isEquals();

	}

	public int hashCode() {
		return new HashCodeBuilder(13, 31) // two randomly chosen prime numbers
			.append(this.getName())
			.append(this.getURI())
			.append(this.getVersion())
			.toHashCode();
	}

}
