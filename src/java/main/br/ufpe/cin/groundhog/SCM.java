package br.ufpe.cin.groundhog;

public enum SCM {
	SOURCE_FORGE, GIT, SVN, HG, UNKNOWN, NONE;

	public String toString() {
		return name();
	}
}