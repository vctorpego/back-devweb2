package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.LocacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocacaoRepository extends JpaRepository<LocacaoModel, Long> {
    List<LocacaoModel> findByClienteId(Long clienteId);
    List<LocacaoModel> findByItemId(Long itemId);

    @Query("SELECT l FROM LocacaoModel l WHERE l.cliente.id = :clienteId AND l.dtDevolucaoEfetiva IS NULL")
    List<LocacaoModel> findLocacoesVigentesByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT l FROM LocacaoModel l WHERE l.item.id = :itemId AND l.dtDevolucaoEfetiva IS NULL")
    Optional<LocacaoModel> findLocacaoVigenteByItemId(@Param("itemId") Long itemId);

    @Query("SELECT l FROM LocacaoModel l WHERE l.dtDevolucaoEfetiva IS NULL AND l.dtDevolucaoPrevista < :dataAtual")
    List<LocacaoModel> findLocacoesEmAtraso(@Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT l FROM LocacaoModel l WHERE l.cliente.id = :clienteId AND l.dtDevolucaoEfetiva IS NULL AND l.dtDevolucaoPrevista < :dataAtual")
    List<LocacaoModel> findLocacoesEmAtrasoByClienteId(@Param("clienteId") Long clienteId, @Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT COUNT(l) FROM LocacaoModel l WHERE l.cliente.id = :clienteId AND l.dtDevolucaoEfetiva IS NULL")
    Long countLocacoesVigentesByClienteId(@Param("clienteId") Long clienteId);
}