package br.ufpe.cin.groundhog;

/**
 * Represents the license used in the project
 * @author ghlp
 * @since 0.0.1
 */
public class License {

	private String name;
	private String entireContent;

	public License(String name) {
		this.name = name;
	}

	public License(String name, String entireContent) {
		this.entireContent = entireContent;
	}

	public String getName() {
		return name;
	}

	public String getEntireContent() {
		return entireContent;
	}
}
