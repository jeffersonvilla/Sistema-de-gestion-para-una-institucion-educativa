package com.ie.fechanotas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ie.fechanotas.business.NotaServicio;
import com.ie.fechanotas.business.UsuarioServicio;
import com.ie.fechanotas.domain.UsuarioNotaDto;
import com.ie.fechanotas.model.Nota;
import com.ie.fechanotas.model.TipoUsuario;
import com.ie.fechanotas.model.Usuario;
import com.ie.fechanotas.rest.NotaControlador;

@RunWith(MockitoJUnitRunner.class)
public class FechanotasApplicationTests {

	@Mock
	private NotaServicio notaServicio;

	@Mock
	private UsuarioServicio usuarioServicio;

	@InjectMocks
	private NotaControlador notaControlador;

	private Usuario usuario;
	private TipoUsuario tipoUsuario;
	private UsuarioNotaDto usuarioNotaDto;
	private Nota nota;
	private List<Nota> notas;

	@Before
	public void setUp() {
		usuario = new Usuario();
		usuario.setIdUsuario(1);
		
		tipoUsuario = new TipoUsuario();

		usuarioNotaDto = new UsuarioNotaDto();
		usuarioNotaDto.setIdUsuario(1);
		usuarioNotaDto.setIdNota(1);
		usuarioNotaDto.setFechaLimite(LocalDateTime.of(2022, Month.FEBRUARY, 7, 16, 20));

		nota = new Nota();
		nota.setFechaLimite(usuarioNotaDto.getFechaLimite());
		nota.setIdNota(1);
		
		Nota nota2 = new Nota();
		nota2.setFechaLimite(LocalDateTime.now());
		nota2.setIdNota(2);

		notas = new ArrayList<>();
		notas.add(nota);
		
		notas.add(nota2);
	}

	@Test
	public void testGetTodasLasNotas_CuandoElUsuarioEsAdministrador_DeberiaDevolverTodasLasNotas() {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuario.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.getAll()).thenReturn(notas);
		ResponseEntity<List<Nota>> response = notaControlador.getTodasLasNotas(usuario);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(notas, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuario.getIdUsuario());
		verify(notaServicio, times(1)).getAll();
	}
	
	@Test
	public void testGetTodasLasNotas_CuandoElUsuarioNoEsAdministrador_DeberiaDevolverForbidden() {
		tipoUsuario.setNombre("normal");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuario.getIdUsuario())).thenReturn(usuario);
		ResponseEntity<List<Nota>> response = notaControlador.getTodasLasNotas(usuario);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuario.getIdUsuario());
	}
	
	@Test
	public void testGetNotaPorId_CuandoElUsuarioEsAdministradorYElIdNotaSeEncuentra_DeberiaDevolverLaNota() {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuario.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.getPorId(nota.getIdNota())).thenReturn(nota);
		ResponseEntity<Nota> response = notaControlador.getNotaPorId(nota.getIdNota(), usuario);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(nota, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuario.getIdUsuario());
		verify(notaServicio, times(1)).getPorId(nota.getIdNota());
	}
	
	@Test
	public void testGetNotaPorId_CuandoElUsuarioEsAdministradorYElIdNotaNoSeEncuentra_DeberiaDevolverNotFoundYBodyNulo() {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuario.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.getPorId(nota.getIdNota())).thenThrow(new NoSuchElementException());
		ResponseEntity<Nota> response = notaControlador.getNotaPorId(nota.getIdNota(), usuario);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuario.getIdUsuario());
		verify(notaServicio, times(1)).getPorId(nota.getIdNota());
	}
	
	@Test
	public void testGetNotaPorId_CuandoElUsuarioNoEsAdministrador_DeberiaDevolverForbiddenYBodyNulo() {
		tipoUsuario.setNombre("normal");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuario.getIdUsuario())).thenReturn(usuario);
		ResponseEntity<Nota> response = notaControlador.getNotaPorId(nota.getIdNota(), usuario);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuario.getIdUsuario());
	}
	
	@Test
	public void testInsertarNota_CuandoElUsuarioEsAdministrador_AtributoFechaLimiteNoEsNull_DeberiaDevolverStatusCreatedYLaNotaInsertada() throws Exception {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.insertar(any(Nota.class))).thenReturn(nota);
		ResponseEntity<Nota> response = notaControlador.insertarNota(usuarioNotaDto);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(nota, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
        verify(notaServicio, times(1)).insertar(any(Nota.class));
	}
	
	@Test
	public void testInsertarNota_CuandoElUsuarioEsAdministrador_AtributoFechaLimiteEsNull_DeberiaDevolverStatusBadRequestYBodyNulo() throws Exception{
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.insertar(any(Nota.class))).thenThrow(new Exception("La fechaLimite no debe ser nula"));
		ResponseEntity<Nota> response = notaControlador.insertarNota(usuarioNotaDto);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
        verify(notaServicio, times(1)).insertar(any(Nota.class));
	}
	
	@Test
	public void testInsertarNota_CuandoElUsuarioNoEsAdministrador_DeberiaDevolverStatusForbiddenYBodyNulo() throws Exception{
		tipoUsuario.setNombre("normal");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		ResponseEntity<Nota> response = notaControlador.insertarNota(usuarioNotaDto);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
	}
	
	@Test
	public void testActualizarNota_CuandoElUsuarioEsAdministrador_LaNotaSeEncuentraPorId_AtributoFechaLimiteNoEsNull_DeberiaDevolverStatusOkYLaNotaActualizada() throws Exception {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.actualizar(any(Nota.class))).thenReturn(nota);
		ResponseEntity<Nota> response = notaControlador.actualizarNota(usuarioNotaDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(nota, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
        verify(notaServicio, times(1)).actualizar(any(Nota.class));
	}
	
	@Test
	public void testActualizarNota_CuandoElUsuarioEsAdministrador_NoSeEncuentraNotaPorId_DeberiaDevolverStatusNotFoundYBodyNulo() throws Exception {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.actualizar(any(Nota.class))).thenThrow(new EntityNotFoundException());
		ResponseEntity<Nota> response = notaControlador.actualizarNota(usuarioNotaDto);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
        verify(notaServicio, times(1)).actualizar(any(Nota.class));
	}
	
	@Test
	public void testActualizarNota_CuandoElUsuarioEsAdministrador_SeEncuentraNotaPorId_AtributoFechaLimiteEsNull_DeberiaDevolverStatusBadRequestYBodyNulo() throws Exception {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		when(notaServicio.actualizar(any(Nota.class))).thenThrow(new Exception());
		ResponseEntity<Nota> response = notaControlador.actualizarNota(usuarioNotaDto);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
        verify(notaServicio, times(1)).actualizar(any(Nota.class));
	}
	
	@Test
	public void testActualizarNota_CuandoElUsuarioNoEsAdministrador_DeberiaDevolverStatusForbiddenYBodyNulo() throws Exception {
		tipoUsuario.setNombre("normal");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		ResponseEntity<Nota> response = notaControlador.actualizarNota(usuarioNotaDto);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
	}
	
	@Test
	public void testEliminarNota_CuandoElUsuarioEsAdministrador_DeberiaDevolverStatusOkYBodyNulo() {
		tipoUsuario.setNombre("administrador");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		ResponseEntity<Nota> response = notaControlador.eliminar(nota.getIdNota(), usuarioNotaDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
	}
	
	@Test
	public void testEliminarNota_CuandoElUsuarioNoEsAdministrador_DeberiaDevolverStatusForbiddenYBodyNulo() {
		tipoUsuario.setNombre("normal");
		usuario.setTipoUsuario(tipoUsuario);
		when(usuarioServicio.getPorId(usuarioNotaDto.getIdUsuario())).thenReturn(usuario);
		ResponseEntity<Nota> response = notaControlador.eliminar(nota.getIdNota(), usuarioNotaDto);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals(null, response.getBody());
		verify(usuarioServicio, times(1)).getPorId(usuarioNotaDto.getIdUsuario());
	}

}
