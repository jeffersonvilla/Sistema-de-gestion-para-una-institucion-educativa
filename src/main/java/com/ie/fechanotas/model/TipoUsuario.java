package com.ie.fechanotas.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="tipo_usuario")
@Data
public class TipoUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idtipousuario")
	private int idTipoUsuario;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUsuario")
	private List<Usuario> usuario;

	@Override
	public String toString() {
		return "TipoUsuario [idTipoUsuario=" + idTipoUsuario + ", nombre=" + nombre + "]";
	}
	
	
	
}
