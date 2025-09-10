package com.locadora.videolocadora.repositories;

import com.locadora.videolocadora.models.SocioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<SocioModel, Long> {
    Optional<SocioModel> findByCpf(String cpf);
    boolean existsByCpf(String cpf);

    List<SocioModel> findByNomeContainingIgnoreCase(String nome);
    List<SocioModel> findByEstahAtivo(Boolean estahAtivo);

    @Query("SELECT s FROM SocioModel s WHERE s.estahAtivo = true AND SIZE(s.dependentes) < 3")
    List<SocioModel> findSociosComVagasParaDependentes();

    @Query("SELECT COUNT(d) FROM DependenteModel d WHERE d.socio.id = :socioId AND d.estahAtivo = true")
    Long countDependentesAtivosBySocioId(@Param("socioId") Long socioId);
}