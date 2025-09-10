package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    Optional<ItemModel> findByNumeroSerie(String numeroSerie);
    boolean existsByNumeroSerie(String numeroSerie);

    @Query("SELECT i FROM ItemModel i WHERE i.titulo.id = :tituloId AND i NOT IN " +
            "(SELECT l.item FROM LocacaoModel l WHERE l.dtDevolucaoEfetiva IS NULL)")
    List<ItemModel> findAvailableByTituloId(@Param("tituloId") Long tituloId);

    @Query("SELECT COUNT(i) FROM ItemModel i WHERE i.titulo.id = :tituloId AND i NOT IN " +
            "(SELECT l.item FROM LocacaoModel l WHERE l.dtDevolucaoEfetiva IS NULL)")
    Long countAvailableByTituloId(@Param("tituloId") Long tituloId);
}
