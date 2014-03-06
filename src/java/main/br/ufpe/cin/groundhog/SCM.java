package br.ufpe.cin.groundhog;

/**
 * Enumerator for the supported source code manager in Groundhog
 * @author fjsj
 * @deprecated
 */
public enum SCM {
	SOURCE_FORGE, GIT, SVN, HG, UNKNOWN, NONE;

	public String toString() {
		return name();
	}
}