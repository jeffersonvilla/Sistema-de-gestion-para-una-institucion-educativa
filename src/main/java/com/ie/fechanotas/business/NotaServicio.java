package com.ie.fechanotas.business;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ie.fechanotas.model.Nota;
import com.ie.fechanotas.repository.NotaRepo;


@Service
public class NotaServicio {

	@Autowired
	private NotaRepo notaRepo;
	
	public List<Nota> getAll(){
		return notaRepo.findAll();
	}
	
	public Nota getPorId(int idNota) throws NoSuchElementException {
		return notaRepo.findById(idNota).orElseThrow(()-> new NoSuchElementException());
	}
	
	public Nota insertar(Nota nota) throws Exception {
		if(nota.getFechaLimite() == null) throw new Exception("La fechalimite no puede ser nula");
		return notaRepo.save(nota);
	}
	
	public Nota actualizar(Nota nota)throws EntityNotFoundException, Exception {
		if(nota.getFechaLimite() == null) throw new Exception("La fechalimite no puede ser nula");
		notaRepo.findById(nota.getIdNota()).orElseThrow(()-> new EntityNotFoundException());
		return notaRepo.save(nota);
	}
	
	public void eliminarPotId(int idNota){
		notaRepo.deleteById(idNota);
	}
}
