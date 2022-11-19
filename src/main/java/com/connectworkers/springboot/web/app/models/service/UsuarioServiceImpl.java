package com.connectworkers.springboot.web.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectworkers.springboot.web.app.models.dao.IPublicacionDao;
import com.connectworkers.springboot.web.app.models.dao.IUsuarioDao;
import com.connectworkers.springboot.web.app.models.entity.Publicacion;
import com.connectworkers.springboot.web.app.models.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IPublicacionDao publicacionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	@Override
	@Transactional
	public void save(Usuario usuario) {
		usuarioDao.save(usuario);

	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findOne(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		usuarioDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findAll(Pageable pageable) {

		return usuarioDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void savePublicacion(Publicacion publicacion) {
		publicacionDao.save(publicacion);
	}

	@Override
	@Transactional(readOnly = true)
	public Publicacion findPublicacionById(Long id) {
		return publicacionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		publicacionDao.deleteById(id);
	}

}
