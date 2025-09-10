package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    Optional<ClienteModel> findByNumInscricao(String numInscricao);
    boolean existsByNumInscricao(String numInscricao);

    List<ClienteModel> findByNomeContainingIgnoreCase(String nome);
    List<ClienteModel> findByEstahAtivo(Boolean estahAtivo);

    @Query("SELECT c FROM ClienteModel c WHERE TYPE(c) = SocioModel")
    List<ClienteModel> findAllSocios();

    @Query("SELECT c FROM ClienteModel c WHERE TYPE(c) = DependenteModel")
    List<ClienteModel> findAllDependentes();
}