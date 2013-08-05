package br.ufpe.cin.groundhog.crawler;

import br.ufpe.cin.groundhog.GroundhogException;

public class DownloadExecption extends GroundhogException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3008366459405858621L;

	public DownloadExecption(String msg) {
		super(msg);
	}

	public DownloadExecption(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DownloadExecption(Throwable cause) {
		super(cause);
	}
}
