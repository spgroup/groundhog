package br.cin.ufpe.epona.scmclient;

import java.util.Date;

public class EmptyProjectAtDateException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private Date date;
	
	public EmptyProjectAtDateException(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
}
