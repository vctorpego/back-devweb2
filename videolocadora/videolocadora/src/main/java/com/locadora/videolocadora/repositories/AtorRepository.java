package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.AtorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtorRepository extends JpaRepository<AtorModel, Long> {
    List<AtorModel> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNome(String nome);
}
