package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.DiretorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiretorRepository extends JpaRepository<DiretorModel, Long> {
    List<DiretorModel> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNome(String nome);
}
