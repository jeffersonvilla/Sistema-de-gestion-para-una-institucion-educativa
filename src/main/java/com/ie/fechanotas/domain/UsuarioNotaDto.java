package com.ie.fechanotas.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioNotaDto {
	
	private int idNota;
	
	private LocalDateTime fechaLimite;
	
	private int idUsuario;

}
