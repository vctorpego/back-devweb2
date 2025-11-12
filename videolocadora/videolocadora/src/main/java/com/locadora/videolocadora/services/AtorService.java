package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.AtorRecordDto;
import com.locadora.videolocadora.models.AtorModel;
import com.locadora.videolocadora.repositories.AtorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtorService {

    @Autowired
    private AtorRepository atorRepository;

    public List<AtorRecordDto> listarTodos() {
        return atorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AtorRecordDto> buscarPorId(Long id) {
        return atorRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<AtorRecordDto> buscarPorNome(String nome) {
        return atorRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AtorRecordDto criar(AtorRecordDto atorRecordDto) {
        if (atorRepository.existsByNome(atorRecordDto.nome())) {
            throw new RuntimeException("Já existe um ator com este nome");
        }

        AtorModel ator = new AtorModel();
        ator.setNome(atorRecordDto.nome());

        AtorModel savedAtor = atorRepository.save(ator);
        return convertToDTO(savedAtor);
    }

    @Transactional
    public Optional<AtorRecordDto> atualizar(Long id, AtorRecordDto atorRecordDto) {
        return atorRepository.findById(id)
                .map(ator -> {
                    if (!ator.getNome().equals(atorRecordDto.nome()) &&
                            atorRepository.existsByNome(atorRecordDto.nome())) {
                        throw new RuntimeException("Já existe um ator com este nome");
                    }

                    ator.setNome(atorRecordDto.nome());
                    AtorModel updatedAtor = atorRepository.save(ator);
                    return convertToDTO(updatedAtor);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return atorRepository.findById(id)
                .map(ator -> {
                    if (!ator.getTitulos().isEmpty()) {
                        throw new RuntimeException("Não é possível excluir ator relacionado a títulos");
                    }

                    atorRepository.delete(ator);
                    return true;
                })
                .orElse(false);
    }

    private AtorRecordDto convertToDTO(AtorModel ator) {
        return new AtorRecordDto(
                ator.getId(),
                ator.getNome(),
                ator.getTitulos() != null ? ator.getTitulos().size() : 0
        );
    }
}