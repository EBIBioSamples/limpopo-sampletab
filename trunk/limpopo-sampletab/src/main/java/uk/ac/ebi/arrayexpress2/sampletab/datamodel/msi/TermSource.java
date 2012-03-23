package uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TermSource {
	private final String name;
	private final String uri;
	private final String version;

	public TermSource(String name, String uri, String version) {
		if (name == null)
		    this.name = null;
		else
		    this.name = name.trim();

        if (uri == null)
            this.uri = null;
        else
            this.uri = uri.trim();
        
        if (version == null)
            this.version = null;
        else
            this.version = version.trim();
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

    @Override
	public boolean equals(Object other) {
		if (other == null){
			return false;
		} else if (other == this){
			return true;
		} else if (!this.getClass().isInstance(other)){
            return false;
		} else {
    		TermSource tsother = (TermSource) other;
    		return new EqualsBuilder()
    			.append(this.getName(), tsother.getName())
    			.append(this.getURI(), tsother.getURI())
    			.append(this.getVersion(), tsother.getVersion())
    			.isEquals();
		}

	}

	@Override
	public int hashCode() {
	    return 7;
//		return new HashCodeBuilder(13, 83) // two randomly chosen prime numbers
//			.append(this.getName())
//			.append(this.getURI())
//			.append(this.getVersion())
//			.toHashCode();
	}

}
