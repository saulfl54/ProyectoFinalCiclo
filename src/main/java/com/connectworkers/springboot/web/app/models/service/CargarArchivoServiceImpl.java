package com.connectworkers.springboot.web.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CargarArchivoServiceImpl implements ICargarArchivoService {


	private final static String DIRECTORIO_UPLOADS = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		Resource recurso = new UrlResource(pathFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar : " + pathFoto.toString());
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String nombreUnicoFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(nombreUnicoFile);


		Files.copy(file.getInputStream(), rootPath);

		return nombreUnicoFile;
	}

	@Override
	public boolean delete(String filename) {
		
		File file =  getPath(filename).toFile();

		if (file.exists() && file.canRead()) {
			if (file.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(DIRECTORIO_UPLOADS).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {//borramos el directorio y todo lo que haya dentro
		FileSystemUtils.deleteRecursively(Paths.get(DIRECTORIO_UPLOADS).toFile());

	}

	@Override
	public void init() throws IOException {//creamos nuevamente el directorio
		Files.createDirectory(Paths.get(DIRECTORIO_UPLOADS));
	}
}
