package com.connectworkers.springboot.web.app.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.connectworkers.springboot.web.app.models.entity.Comentario;
import com.connectworkers.springboot.web.app.models.entity.Publicacion;
import com.connectworkers.springboot.web.app.models.entity.Usuario;
import com.connectworkers.springboot.web.app.models.service.ICargarArchivoService;
import com.connectworkers.springboot.web.app.models.service.IUsuarioService;
import com.connectworkers.springboot.web.app.util.Paginador;

@Controller
@RequestMapping("/publicacion")
@SessionAttributes("publicacion")
public class PublicacionController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ICargarArchivoService cargarArchivoService;
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		int size = 10;// cantidad a mostrar por pagina
		Pageable pageRequest = PageRequest.of(page, size);

		Page<Publicacion> publicaciones = usuarioService.findAllPublicaciones(pageRequest);
		// url,Page<Publicacion>
		Paginador<Publicacion> pageRender = new Paginador<>("/home", publicaciones);

		model.addAttribute("titulo", "Listado de Publicaciones");
		model.addAttribute("page", pageRender);
		model.addAttribute("publicaciones", publicaciones);
		
		return "publicacion/listar";
	}

	@GetMapping("/form/{usuarioId}")
	public String crear(@PathVariable(value = "usuarioId") Long usuarioId, Map<String, Object> model,
			RedirectAttributes flash) {

		Usuario usuario = usuarioService.findOne(usuarioId);

		if (usuario == null) {
			flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
			return "redirect:/listar";
		}

		Publicacion publicacion = new Publicacion();
		publicacion.setUsuario(usuario);

		model.put("publicacion", publicacion);
		model.put("titulo", "Crear Publicación");

		return "publicacion/form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Publicacion publicacion, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status, @RequestParam("file") MultipartFile file) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Publicación");
			return "publicacion/form";
		}

		if (!file.isEmpty()) {

			if (publicacion.getId() != null && publicacion.getId() > 0 && publicacion.getImagen() != null
					&& publicacion.getImagen().length() > 0) {

				cargarArchivoService.delete(publicacion.getImagen());
			}

			String nombreUnicoFile = null;
			try {
				nombreUnicoFile = cargarArchivoService.copy(file);
			} catch (IOException e) {

				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Se ha subido con éxito '" + nombreUnicoFile + "'");

			publicacion.setImagen(nombreUnicoFile);
		}

		String mensajeFlash = (publicacion.getId() != null) ? "Publicación editada con éxito!"
				: "Publicación creada con éxito!";

		usuarioService.savePublicacion(publicacion);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/usuario/listar";
	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Publicacion publicacion = usuarioService.findPublicacionById(id);

		if (publicacion == null) {
			flash.addFlashAttribute("error", "Publicación inexistente en la base de datos!");
			return "redirect:/listar";
		}

		model.addAttribute("publicacion", publicacion);
		model.addAttribute("titulo", publicacion.getTitulo());

		return "publicacion/ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		Publicacion publicacion = usuarioService.findPublicacionById(id);
		
		if(publicacion != null) {
			usuarioService.deleteFactura(id);
			flash.addFlashAttribute("success", "Publicación eliminada con éxito!");
			return "redirect:/usuario/ver/" + publicacion.getUsuario().getId();
		}
		flash.addFlashAttribute("error", "No se pudo eliminar, no existe en la base de datos!");
		
		return "redirect:/listar";
	}
	
	
	
	@PostMapping("/comentar/{publicacionId}")
	public String comentar(Comentario comentario,@PathVariable(value = "publicacionId") Long publicacionId, Map<String, Object> model,
			RedirectAttributes flash) {

		Publicacion publicacion = usuarioService.findPublicacionById(publicacionId);

		if (publicacion == null) {
			flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
			return "redirect:/listar";
		}

	
		comentario.setUsuario(usuarioService.findOne((long) 1));
		publicacion.addComentario(comentario);

		usuarioService.saveComentario(comentario);

		model.put("publicacion", publicacion);
		model.put("titulo", publicacion.getTitulo());

		return "redirect:/publicacion/ver/{publicacionId}";
	}
	
	

}
