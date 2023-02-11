package com.ie.fechanotas.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ie.fechanotas.model.Usuario;
import com.ie.fechanotas.repository.UsuarioRepo;

@Service
public class UsuarioServicio {

	@Autowired
	private UsuarioRepo usuarioRepo;
	
	public Usuario getPorId(int idUsuario) {
		return usuarioRepo.findById(idUsuario).get();
	}
}
