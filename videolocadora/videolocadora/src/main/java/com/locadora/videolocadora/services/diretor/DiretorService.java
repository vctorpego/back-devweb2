package com.locadora.videolocadora.services.diretor;

import com.locadora.videolocadora.dtos.DiretorRecordDto;
import com.locadora.videolocadora.exceptions.DiretorNaoEncontradoException;
import com.locadora.videolocadora.models.DiretorModel;
import com.locadora.videolocadora.repositories.DiretorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiretorService {

    @Autowired
    private DiretorRepository diretorRepository;

    public List<DiretorRecordDto> listarTodos() {
        return diretorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DiretorRecordDto> buscarPorId(Long id) {
        return Optional.ofNullable(diretorRepository.findById(id)
                .map(this::convertToDTO).orElseThrow(() -> new DiretorNaoEncontradoException("Nao foi possivel encontarr o diretor com o id inserido!")));
    }

    public List<DiretorRecordDto> buscarPorNome(String nome) {
        return diretorRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiretorRecordDto criar(DiretorRecordDto diretorRecordDto) {
        if (diretorRepository.existsByNome(diretorRecordDto.nome())) {
            throw new RuntimeException("Já existe um diretor com este nome");
        }

        DiretorModel diretor = new DiretorModel();
        diretor.setNome(diretorRecordDto.nome());

        DiretorModel savedDiretor = diretorRepository.save(diretor);
        return convertToDTO(savedDiretor);
    }

    @Transactional
    public Optional<DiretorRecordDto> atualizar(Long id, DiretorRecordDto diretorRecordDto) {
        return diretorRepository.findById(id)
                .map(diretor -> {
                    if (!diretor.getNome().equals(diretorRecordDto.nome()) &&
                            diretorRepository.existsByNome(diretorRecordDto.nome())) {
                        throw new RuntimeException("Já existe um diretor com este nome");
                    }

                    diretor.setNome(diretorRecordDto.nome());
                    DiretorModel updatedDiretor = diretorRepository.save(diretor);
                    return convertToDTO(updatedDiretor);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return diretorRepository.findById(id)
                .map(diretor -> {
                    if (!diretor.getTitulos().isEmpty()) {
                        throw new RuntimeException("Não é possível excluir diretor relacionado a títulos");
                    }

                    diretorRepository.delete(diretor);
                    return true;
                })
                .orElse(false);
    }

    private DiretorRecordDto convertToDTO(DiretorModel diretor) {
        return new DiretorRecordDto(diretor.getId(), diretor.getNome(), diretor.getTitulos()!= null ? diretor.getTitulos().size() : 0);
    }
}