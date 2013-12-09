package br.ufpe.cin.groundhog;

import org.mongodb.morphia.annotations.Entity;

/**
 * Represents the License used in the {@link Project}
 * @author ghlp
 * @since 0.0.1
 */
@Entity("licenses")
public class License {

	private String name;
	private String entireContent;
	private String version;

	public License(String name) {
		this.name = name;
	}

	public License(String name, String entireContent) {
		this.entireContent = entireContent;
	}

	public String getName() {
		return this.name;
	}

	public String getEntireContent() {
		return this.entireContent;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public String toString() {
		return this.name;
	}
	
	/**
	 * Two {@link License} objects are considered equal when they both have the same name and version
	 * @param lic
	 * @return
	 */
	public boolean equals(License lic) {
		return this.name == lic.getName() && this.version == lic.version;
	}
}