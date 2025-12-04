package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.SocioRecordDto;
import com.locadora.videolocadora.models.ClienteModel.Sexo;
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
public class SocioService {

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private DependenteRepository dependenteRepository;

    public List<SocioRecordDto> listarTodos() {
        return socioRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<SocioRecordDto> buscarPorId(Long id) {
        return socioRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<SocioRecordDto> buscarPorCpf(String cpf) {
        return socioRepository.findByCpf(cpf)
                .map(this::convertToDto);
    }

    public List<SocioRecordDto> buscarPorNome(String nome) {
        return socioRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<SocioRecordDto> buscarPorStatus(Boolean estahAtivo) {
        return socioRepository.findByEstahAtivo(estahAtivo).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<SocioRecordDto> buscarSociosComVagasParaDependentes() {
        return socioRepository.findSociosComVagasParaDependentes().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public SocioRecordDto criar(SocioRecordDto socioDto) {
        if (socioRepository.existsByCpf(socioDto.cpf())) {
            throw new RuntimeException("Já existe um sócio com este CPF");
        }

        // Gerar número de inscrição automático (exemplo simples)
        String numInscricao = "SOC" + System.currentTimeMillis();

        SocioModel socio = new SocioModel();
        socio.setNumInscricao(numInscricao);
        socio.setNome(socioDto.nome());
        socio.setDtNascimento(socioDto.dtNascimento());
        socio.setSexo(socioDto.sexo());
        socio.setCpf(socioDto.cpf());
        socio.setEndereco(socioDto.endereco());
        socio.setTelefone(socioDto.telefone());

        SocioModel savedSocio = socioRepository.save(socio);
        return convertToDto(savedSocio);
    }

    @Transactional
    public Optional<SocioRecordDto> atualizar(Long id, SocioRecordDto socioDto) {
        System.out.println("===== DEBUG BACKEND =====");
        System.out.println("ID: " + id);
        System.out.println("DTO: " + socioDto);
        System.out.println("estahAtivo RECEBIDO = " + socioDto.estahAtivo());
        System.out.println("==========================");
        return socioRepository.findById(id)
                .map(socio -> {
                    if (!socio.getCpf().equals(socioDto.cpf()) &&
                            socioRepository.existsByCpf(socioDto.cpf())) {
                        throw new RuntimeException("Já existe um sócio com este CPF");
                    }


                    socio.setNome(socioDto.nome());
                    socio.setDtNascimento(socioDto.dtNascimento());
                    socio.setSexo(socioDto.sexo());
                    socio.setCpf(socioDto.cpf());
                    socio.setEndereco(socioDto.endereco());
                    socio.setTelefone(socioDto.telefone());
                    socio.setEstahAtivo(socioDto.estahAtivo());
                    if (!socioDto.estahAtivo()) {
                        socio.getDependentes().forEach(dep -> dep.setEstahAtivo(false));
                    }

                    SocioModel updatedSocio = socioRepository.save(socio);
                    return convertToDto(updatedSocio);
                });
    }

    @Transactional
    public Optional<SocioRecordDto> desativar(Long id) {
        return socioRepository.findById(id)
                .map(socio -> {
                    socio.setEstahAtivo(false);
                    // Desativar dependentes também
                    socio.getDependentes().forEach(dependente -> dependente.setEstahAtivo(false));

                    SocioModel updatedSocio = socioRepository.save(socio);
                    return convertToDto(updatedSocio);
                });
    }

    @Transactional
    public Optional<SocioRecordDto> reativar(Long id) {
        return socioRepository.findById(id)
                .map(socio -> {
                    socio.setEstahAtivo(true);
                    // Reativar até 3 dependentes
                    socio.getDependentes().stream()
                            .limit(3)
                            .forEach(dependente -> dependente.setEstahAtivo(true));

                    SocioModel updatedSocio = socioRepository.save(socio);
                    return convertToDto(updatedSocio);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return socioRepository.findById(id)
                .map(socio -> {
                    if (!socio.getLocacoes().isEmpty()) {
                        throw new RuntimeException("Não é possível excluir sócio que possui locações");
                    }

                    socioRepository.delete(socio);
                    return true;
                })
                .orElse(false);
    }

    private SocioRecordDto convertToDto(SocioModel socio) {
        return new SocioRecordDto(
                socio.getId(),
                socio.getNumInscricao(),
                socio.getNome(),
                socio.getDtNascimento(),
                socio.getSexo(),
                socio.getEstahAtivo(),
                socio.getCpf(),
                socio.getEndereco(),
                socio.getTelefone(),
                (int) socio.getQuantidadeDependentesAtivos()
        );
    }
}