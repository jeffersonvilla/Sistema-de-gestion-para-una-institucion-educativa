package com.ie.fechanotas.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

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
