package com.connectworkers.springboot.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.connectworkers.springboot.web.app.models.entity.Publicacion;

public interface IPublicacionDao extends CrudRepository<Publicacion, Long>{

}