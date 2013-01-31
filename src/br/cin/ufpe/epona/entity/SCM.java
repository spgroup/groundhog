package br.cin.ufpe.epona.entity;

public enum SCM {
	SOURCE_FORGE, GIT, SVN, HG, UNKNOWN, NONE;

	public String toString() {
		return name();
	}
}
