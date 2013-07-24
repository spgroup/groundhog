package br.ufpe.cin.groundhog;

/**
 * Represents languages - an important set of components of a {@link Project} -
 * in Groundhog. This class is only important/meaningful in the context of
 * Projects. As a representation of the programming language composition of such
 * objects.
 * 
 * @author Rodrigo Alves
 * @since 0.0.1
 * 
 */
public class Language implements Comparable<Language> {
	public static final String JAVA = "Java";
	
	private String name;
	private int loc;
	
	public Language(String name, int loc) {
		this.name = name;
		this.loc = loc;
	}

	/**
	 * Informs the language name
	 * @return a {@link String} object
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Informs the loc (lines of code) of the language in its project
	 *  @return a {@link String} object
	 */
	public int getLoc() {
		return this.loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}

	@Override
	public int compareTo(Language o) {
		if( this.getLoc() < o.getLoc()){
			return -1;
		} 
		if( this.getLoc() > o.getLoc()){
			return 1;
		}
		return 0;
	}
	
	public String toString() {
		return name;
	}
}