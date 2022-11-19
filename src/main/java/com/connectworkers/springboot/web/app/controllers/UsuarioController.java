package com.connectworkers.springboot.web.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.connectworkers.springboot.web.app.models.entity.Usuario;
import com.connectworkers.springboot.web.app.models.service.ICargarArchivoService;
import com.connectworkers.springboot.web.app.models.service.IUsuarioService;
import com.connectworkers.springboot.web.app.util.Paginador;

@Controller
@RequestMapping("usuario")
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ICargarArchivoService cargarArchivoService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		int size = 10;// cantidad a mostrar por pagina
		Pageable pageRequest = PageRequest.of(page, size);

		Page<Usuario> usuarios = usuarioService.findAll(pageRequest);
		// url,Page<Usuario>
		Paginador<Usuario> pageRender = new Paginador<>("/usuario/listar", usuarios);

		model.addAttribute("titulo", "Listado de usuarios");
		model.addAttribute("page", pageRender);
		model.addAttribute("usuarios", usuarios);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Usuario usuario = new Usuario();
		model.put("usuario", usuario);
		model.put("titulo", "Formulario de Usuario");
		return "form";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = usuarioService.findOne(id);

			if (usuario == null) {
				flash.addFlashAttribute("error", "Usuario inexistente en la base de datos!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El identificador del usuario no puede ser cero!");
			return "redirect:/usuario/listar";
		}
		model.put("usuario", usuario);
		model.put("titulo", "Editar Usuario");
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status, @RequestParam("file") MultipartFile foto) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Usuario");
			return "form";
		}

		if (!foto.isEmpty()) {

			if (usuario.getId() != null && usuario.getId() > 0 && usuario.getFoto() != null
					&& usuario.getFoto().length() > 0) {

				cargarArchivoService.delete(usuario.getFoto());
			}

			String nombreUnicoFile = null;
			try {
				nombreUnicoFile = cargarArchivoService.copy(foto);
			} catch (IOException e) {

				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Se ha subido con éxito '" + nombreUnicoFile + "'");

			usuario.setFoto(nombreUnicoFile);
		}

		String mensajeFlash = (usuario.getId() != null) ? "Usuario editado con éxito!" : "Usuario creado con éxito!";

		usuarioService.save(usuario);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/usuario/listar";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Usuario usuario = usuarioService.findOne(id);
			usuarioService.delete(id);
			flash.addFlashAttribute("success", "Usuario eliminado con éxito!");

			if(usuario.getFoto()!=null) {
				if (cargarArchivoService.delete(usuario.getFoto())) {
					flash.addFlashAttribute("info", "Foto " + usuario.getFoto() + " eliminada con éxito!");
				}
			}

		}

		return "redirect:/usuario/listar";
	}

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {
			recurso = cargarArchivoService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Usuario usuario = usuarioService.findOne(id);
		if (usuario == null) {
			flash.addFlashAttribute("error", "Usuario inexistente en la base de datos!");
			return "redirect:/listar";
		}

		model.put("usuario", usuario);
		model.put("titulo", "Perfil de " + usuario.getUsername());
		return "ver";
	}
}