package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.TituloModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TituloRepository extends JpaRepository<TituloModel, Long> {
    List<TituloModel> findByNomeContainingIgnoreCase(String nome);
    List<TituloModel> findByCategoriaContainingIgnoreCase(String categoria);

    @Query("SELECT t FROM TituloModel t JOIN t.atores a WHERE a.nome LIKE %:nomeAtor%")
    List<TituloModel> findByAtorNome(@Param("nomeAtor") String nomeAtor);

    boolean existsByNome(String nome);
}
