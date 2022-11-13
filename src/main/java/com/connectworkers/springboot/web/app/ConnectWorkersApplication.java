package com.connectworkers.springboot.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.connectworkers.springboot.web.app.models.service.ICargarArchivoService;

@SpringBootApplication
public class ConnectWorkersApplication implements CommandLineRunner {

	@Autowired
	ICargarArchivoService cargarArchivoService;

	public static void main(String[] args) {
		SpringApplication.run(ConnectWorkersApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		cargarArchivoService.deleteAll();
		cargarArchivoService.init();
	}
}
