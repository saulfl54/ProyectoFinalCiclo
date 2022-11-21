package com.connectworkers.springboot.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.connectworkers.springboot.web.app.models.entity.Comentario;



public interface IComentarioDao extends CrudRepository<Comentario, Long>{

}
