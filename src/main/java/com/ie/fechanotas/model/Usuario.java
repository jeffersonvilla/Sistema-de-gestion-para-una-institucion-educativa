package com.ie.fechanotas.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="usuario")
@Data
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idusuario")
	private int idUsuario;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = TipoUsuario.class)
	@JoinColumn(name = "idtipousuario", referencedColumnName = "idtipousuario", nullable = false)
	private TipoUsuario tipoUsuario;
	
}
