package com.ie.fechanotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ie.fechanotas.model.Nota;

@Repository
public interface NotaRepo extends JpaRepository<Nota, Integer> {

}
