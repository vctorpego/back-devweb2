package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.ClienteRecordDto;
import com.locadora.videolocadora.models.ClienteModel;
import com.locadora.videolocadora.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteRecordDto> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<ClienteRecordDto> buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<ClienteRecordDto> buscarPorNumInscricao(String numInscricao) {
        return clienteRepository.findByNumInscricao(numInscricao)
                .map(this::convertToDto);
    }

    public List<ClienteRecordDto> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClienteRecordDto> buscarPorStatus(Boolean estahAtivo) {
        return clienteRepository.findByEstahAtivo(estahAtivo).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClienteRecordDto> listarSocios() {
        return clienteRepository.findAllSocios().stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClienteRecordDto> listarDependentes() {
        return clienteRepository.findAllDependentes().stream()
                .map(this::convertToDto)
                .toList();
    }

    private ClienteRecordDto convertToDto(ClienteModel cliente) {
        String tipoCliente = cliente instanceof com.locadora.videolocadora.models.SocioModel ? "SOCIO" : "DEPENDENTE";

        return new ClienteRecordDto(
                cliente.getId(),
                cliente.getNumInscricao(),
                cliente.getNome(),
                cliente.getDtNascimento(),
                cliente.getSexo(),
                cliente.getEstahAtivo(),
                tipoCliente
        );
    }
}