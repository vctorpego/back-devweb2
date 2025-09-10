package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.DependenteRecordDto;
import com.locadora.videolocadora.models.ClienteModel.Sexo;
import com.locadora.videolocadora.models.DependenteModel;
import com.locadora.videolocadora.models.SocioModel;
import com.locadora.videolocadora.repositories.DependenteRepository;
import com.locadora.videolocadora.repositories.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DependenteService {

    @Autowired
    private DependenteRepository dependenteRepository;

    @Autowired
    private SocioRepository socioRepository;

    public List<DependenteRecordDto> listarTodos() {
        return dependenteRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<DependenteRecordDto> buscarPorId(Long id) {
        return dependenteRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<DependenteRecordDto> buscarPorSocioId(Long socioId) {
        return dependenteRepository.findBySocioId(socioId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<DependenteRecordDto> buscarPorSocioIdEStatus(Long socioId, Boolean estahAtivo) {
        return dependenteRepository.findBySocioIdAndEstahAtivo(socioId, estahAtivo).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public DependenteRecordDto criar(DependenteRecordDto dependenteDto) {
        SocioModel socio = socioRepository.findById(dependenteDto.socioId())
                .orElseThrow(() -> new RuntimeException("Sócio não encontrado"));

        // Verificar se o sócio pode ter mais dependentes
        Long quantidadeDependentesAtivos = socioRepository.countDependentesAtivosBySocioId(socio.getId());
        if (quantidadeDependentesAtivos >= 3) {
            throw new RuntimeException("Sócio já possui 3 dependentes ativos");
        }

        // Verificar se o sócio está ativo
        if (!socio.getEstahAtivo()) {
            throw new RuntimeException("Sócio não está ativo");
        }

        // Gerar número de inscrição automático
        String numInscricao = "DEP" + System.currentTimeMillis();

        DependenteModel dependente = new DependenteModel();
        dependente.setNumInscricao(numInscricao);
        dependente.setNome(dependenteDto.nome());
        dependente.setDtNascimento(dependenteDto.dtNascimento());
        dependente.setSexo(dependenteDto.sexo());
        dependente.setSocio(socio);

        DependenteModel savedDependente = dependenteRepository.save(dependente);
        return convertToDto(savedDependente);
    }

    @Transactional
    public Optional<DependenteRecordDto> atualizar(Long id, DependenteRecordDto dependenteDto) {
        return dependenteRepository.findById(id)
                .map(dependente -> {
                    dependente.setNome(dependenteDto.nome());
                    dependente.setDtNascimento(dependenteDto.dtNascimento());
                    dependente.setSexo(dependenteDto.sexo());

                    DependenteModel updatedDependente = dependenteRepository.save(dependente);
                    return convertToDto(updatedDependente);
                });
    }

    @Transactional
    public Optional<DependenteRecordDto> desativar(Long id) {
        return dependenteRepository.findById(id)
                .map(dependente -> {
                    dependente.setEstahAtivo(false);
                    DependenteModel updatedDependente = dependenteRepository.save(dependente);
                    return convertToDto(updatedDependente);
                });
    }

    @Transactional
    public Optional<DependenteRecordDto> reativar(Long id) {
        return dependenteRepository.findById(id)
                .map(dependente -> {
                    // Verificar se o sócio pode ter mais dependentes ativos
                    Long quantidadeDependentesAtivos = socioRepository.countDependentesAtivosBySocioId(dependente.getSocio().getId());
                    if (quantidadeDependentesAtivos >= 3) {
                        throw new RuntimeException("Sócio já possui 3 dependentes ativos");
                    }

                    dependente.setEstahAtivo(true);
                    DependenteModel updatedDependente = dependenteRepository.save(dependente);
                    return convertToDto(updatedDependente);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return dependenteRepository.findById(id)
                .map(dependente -> {
                    if (!dependente.getLocacoes().isEmpty()) {
                        throw new RuntimeException("Não é possível excluir dependente que possui locações");
                    }

                    dependenteRepository.delete(dependente);
                    return true;
                })
                .orElse(false);
    }

    private DependenteRecordDto convertToDto(DependenteModel dependente) {
        return new DependenteRecordDto(
                dependente.getId(),
                dependente.getNumInscricao(),
                dependente.getNome(),
                dependente.getDtNascimento(),
                dependente.getSexo(),
                dependente.getEstahAtivo(),
                dependente.getSocio().getId(),
                dependente.getSocio().getNome()
        );
    }
}