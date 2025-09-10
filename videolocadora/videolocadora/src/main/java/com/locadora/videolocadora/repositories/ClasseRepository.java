package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.ClasseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<ClasseModel, Long> {
    List<ClasseModel> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNome(String nome);
}
