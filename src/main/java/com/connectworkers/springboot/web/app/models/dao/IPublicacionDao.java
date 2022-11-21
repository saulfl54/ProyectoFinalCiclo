package com.connectworkers.springboot.web.app.models.dao;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.connectworkers.springboot.web.app.models.entity.Publicacion;


//public interface IPublicacionDao extends CrudRepository<Publicacion, Long>{
public interface IPublicacionDao extends PagingAndSortingRepository<Publicacion, Long> {

}