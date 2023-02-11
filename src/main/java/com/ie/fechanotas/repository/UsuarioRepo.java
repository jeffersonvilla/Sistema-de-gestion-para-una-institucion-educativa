package com.ie.fechanotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ie.fechanotas.model.Usuario;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer>{

}
