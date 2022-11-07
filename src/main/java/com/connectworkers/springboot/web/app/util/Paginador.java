package com.connectworkers.springboot.web.app.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;


public class Paginador<T> {

	// CONSTRUCTOR

	//recibe como parametro la url y la lista paginada
	public Paginador(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PaginaItem>(); //inciamos la lista con paginaitem


		numElementosPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;//para que en vez 0 la primera sea 1
		int desde, hasta;

	
		if (totalPaginas <= numElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {//3 rangos
			if (paginaActual <= numElementosPorPagina / 2) {
				desde = 1;
				hasta = numElementosPorPagina;
			} else if (paginaActual >= totalPaginas - numElementosPorPagina / 2) {
				desde = totalPaginas - numElementosPorPagina + 1;
				hasta = numElementosPorPagina;
			} else {
				desde = paginaActual - numElementosPorPagina / 2;
				hasta = numElementosPorPagina;
			}
		}
		
		//rellenamos la lista

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PaginaItem(desde + i, paginaActual == desde + i));
		}

	}
	// ----------------Fin constructor-------------------------------
	
	
	//Getter para poder acceder desde la vista.

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PaginaItem> getPaginas() {
		return paginas;
	}
	
	//para crear un link a la primera, A la última, a la siguiente, a la anterior.

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	// campos de clase
	private String url;

	private Page<T> page;// lista paginada

	private int totalPaginas;

	private int numElementosPorPagina;

	private int paginaActual;

	private List<PaginaItem> paginas;

}
