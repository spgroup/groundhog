package br.ufpe.cin.groundhog.crawler;

import br.ufpe.cin.groundhog.GroundhogException;

public class DownloadException extends GroundhogException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3008366459405858621L;

	public DownloadException(String msg) {
		super(msg);
	}

	public DownloadException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DownloadException(Throwable cause) {
		super(cause);
	}
}
