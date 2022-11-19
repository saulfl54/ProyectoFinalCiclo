package com.connectworkers.springboot.web.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.connectworkers.springboot.web.app.models.entity.Publicacion;
import com.connectworkers.springboot.web.app.models.entity.Usuario;


public interface IUsuarioService {

	public List<Usuario> findAll();
	
	public Page<Usuario> findAll(Pageable pageable);

	public void save(Usuario usuario);
	
	public Usuario findOne(Long id);
	
	public void delete(Long id);

	public void savePublicacion(Publicacion publicacion);

	public Publicacion findPublicacionById(Long id);

	void deleteFactura(Long id);
	
	
	
}
