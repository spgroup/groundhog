package br.cin.ufpe.epona.codehistory;

import br.cin.ufpe.epona.entity.SCM;

public class UnsupportedSCMException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private SCM scm; 
	
	public UnsupportedSCMException(SCM scm) {
		this.scm = scm;
	}

	public SCM getSCM() {
		return scm;
	}
	
}
