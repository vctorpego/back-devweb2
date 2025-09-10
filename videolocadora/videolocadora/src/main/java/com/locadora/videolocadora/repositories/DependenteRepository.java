package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.DependenteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DependenteRepository extends JpaRepository<DependenteModel, Long> {
    List<DependenteModel> findBySocioId(Long socioId);
    List<DependenteModel> findBySocioIdAndEstahAtivo(Long socioId, Boolean estahAtivo);

    @Query("SELECT d FROM DependenteModel d WHERE d.socio.id = :socioId AND d.estahAtivo = true")
    List<DependenteModel> findDependentesAtivosBySocioId(@Param("socioId") Long socioId);
}