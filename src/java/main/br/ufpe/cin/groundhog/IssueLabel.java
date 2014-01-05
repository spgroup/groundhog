package br.ufpe.cin.groundhog;

/**
 * This class does not represent a full-fledged GitHub entity.
 *  It is just for the Issue labels
 *  
 * @author Rodrigo Alves
 *
 */
public class IssueLabel {
	private String url;
	private String name;
	private String color;
	
	public IssueLabel(String url, String name, String color) {
		this.url = url;
		this.name = name;
		this.color = color;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "IssueLabel [" + (url != null ? "url=" + url + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (color != null ? "color=" + color : "") + "]";
	}
}