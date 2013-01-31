package br.cin.ufpe.epona;

public class Project {
	private String name;
	private String description;
	private String creator;
	private String iconURL;
	private SCM scm;
	private String scmURL;
	
	public Project() {

	}
	
	public Project(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Project(String name, String description, String iconURL) {
		this(name, description);
		this.iconURL = iconURL;
	}
	
	public Project(String name, String description, String iconURL, SCM scm, String scmURL) {
		this(name, description, iconURL);
		this.scm = scm;
		this.scmURL = scmURL;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	public SCM getSCM() {
		return scm;
	}
	
	public void setSCM(SCM scm) {
		this.scm = scm;
	}
	
	public String getScmURL() {
		return scmURL;
	}
	
	public void setScmURL(String scmURL) {
		this.scmURL = scmURL;
	}
	
	@Override
	public String toString() {
		return String.format("Project(%s, %s, %s)", name, description, iconURL);
	}
	
}
