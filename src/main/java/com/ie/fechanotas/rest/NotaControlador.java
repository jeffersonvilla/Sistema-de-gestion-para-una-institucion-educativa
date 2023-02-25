package com.ie.fechanotas.rest;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ie.fechanotas.business.NotaServicio;
import com.ie.fechanotas.business.UsuarioServicio;
import com.ie.fechanotas.domain.UsuarioNotaDto;
import com.ie.fechanotas.model.Nota;
import com.ie.fechanotas.model.Usuario;

@RestController
@RequestMapping("/nota")
public class NotaControlador {
	
	private final String ADMINISTRADOR_TAG = "administrador";

	@Autowired
	private NotaServicio notaServicio;

	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@GetMapping("/todas")
	public ResponseEntity<List<Nota>> getTodasLasNotas(@RequestBody Usuario usuario){
		if(usuarioServicio.getPorId(usuario.getIdUsuario()).getTipoUsuario().getNombre().equals(ADMINISTRADOR_TAG))
			return new ResponseEntity<List<Nota>>(notaServicio.getAll(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/id/{idNota}")
	public ResponseEntity<Nota> getNotaPorId(@PathVariable("idNota") int idNota, @RequestBody Usuario usuario) {
		if(usuarioServicio.getPorId(usuario.getIdUsuario()).getTipoUsuario().getNombre().equals(ADMINISTRADOR_TAG)) {
			try {
				return new ResponseEntity<Nota>(notaServicio.getPorId(idNota), HttpStatus.OK);
			}catch (NoSuchElementException e) {
				e.printStackTrace();
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@PostMapping("/insertar")
	public ResponseEntity<Nota> insertarNota(@RequestBody UsuarioNotaDto usuarioNotaDto) {
		if(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario()).getTipoUsuario().getNombre().equals(ADMINISTRADOR_TAG)) {
			try {
				Nota nota = new Nota();
				nota.setFechaLimite(usuarioNotaDto.getFechaLimite());
				return new ResponseEntity<Nota>(notaServicio.insertar(nota), HttpStatus.CREATED);
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@PutMapping("/actualizar")
	public ResponseEntity<Nota> actualizarNota(@RequestBody UsuarioNotaDto usuarioNotaDto) {
		if(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario()).getTipoUsuario().getNombre().equals(ADMINISTRADOR_TAG)) {
			try {
				Nota nota = new Nota();
				nota.setFechaLimite(usuarioNotaDto.getFechaLimite());
				nota.setIdNota(usuarioNotaDto.getIdNota());
				return new ResponseEntity<Nota>(notaServicio.actualizar(nota), HttpStatus.OK);
			}catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}catch (Exception e) {
				System.out.println(e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@DeleteMapping("/eliminar/id/{idNota}")
	public ResponseEntity<Nota> eliminar(@PathVariable("idNota") int idNota, @RequestBody UsuarioNotaDto usuarioNotaDto) {
		if(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario()).getTipoUsuario().getNombre().equals(ADMINISTRADOR_TAG)) {
			notaServicio.eliminarPotId(idNota);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}
