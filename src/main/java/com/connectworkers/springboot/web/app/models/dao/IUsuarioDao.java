package com.connectworkers.springboot.web.app.models.dao;


//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import com.connectworkers.springboot.web.app.models.entity.Usuario;

//public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
public interface IUsuarioDao extends PagingAndSortingRepository<Usuario, Long> {
	
	

}
