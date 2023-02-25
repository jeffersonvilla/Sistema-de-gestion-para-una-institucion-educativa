package com.ie.fechanotas.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

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
