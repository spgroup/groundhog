package br.ufpe.cin.groundhog.http;

import br.ufpe.cin.groundhog.GroundhogException;

public class HttpException extends GroundhogException {
	private static final long serialVersionUID = 5824357504573919990L;

	public HttpException(String msg) {
		super(msg);
	}

	public HttpException(Throwable cause) {
		super(cause);
	}

	public HttpException(String msg, Throwable cause) {
		super(msg, cause);
	}
}