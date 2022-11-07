package com.connectworkers.springboot.web.app.util;

public class PaginaItem {

	private int numero;//numero de pagina
	private boolean actual;//si es actual o no.

	public PaginaItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}

}
