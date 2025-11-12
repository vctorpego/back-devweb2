package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.ClasseRecordDto;
import com.locadora.videolocadora.models.ClasseModel;
import com.locadora.videolocadora.repositories.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClasseService {

    @Autowired
    private ClasseRepository classeRepository;

    public List<ClasseRecordDto> listarTodos() {
        return classeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClasseRecordDto> buscarPorId(Long id) {
        return classeRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<ClasseRecordDto> buscarPorNome(String nome) {
        return classeRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClasseRecordDto criar(ClasseRecordDto classeRecordDto) {
        if (classeRepository.existsByNome(classeRecordDto.nome())) {
            throw new RuntimeException("Já existe uma classe com este nome");
        }
        ClasseModel classe = new ClasseModel();
        classe.setNome(classeRecordDto.nome());
        classe.setValor(classeRecordDto.valor());
        classe.setPrazoDevolucao(classeRecordDto.prazoDevolucao());

        ClasseModel savedClasse = classeRepository.save(classe);
        return convertToDTO(savedClasse);
    }

    @Transactional
    public Optional<ClasseRecordDto> atualizar(Long id, ClasseRecordDto classeRecordDto) {
        return classeRepository.findById(id)
                .map(classe -> {
                    if (!classe.getNome().equals(classeRecordDto.nome()) &&
                            classeRepository.existsByNome(classeRecordDto.nome())) {
                        throw new RuntimeException("Já existe uma classe com este nome");
                    }

                    classe.setNome(classeRecordDto.nome());
                    classe.setValor(classeRecordDto.valor());
                    classe.setPrazoDevolucao(classeRecordDto.prazoDevolucao());

                    ClasseModel updatedClasse = classeRepository.save(classe);
                    return convertToDTO(updatedClasse);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return classeRepository.findById(id)
                .map(classe -> {
                    if (!classe.getTitulos().isEmpty()) {
                        throw new RuntimeException("Não é possível excluir classe relacionada a títulos");
                    }

                    classeRepository.delete(classe);
                    return true;
                })
                .orElse(false);
    }

    private ClasseRecordDto convertToDTO(ClasseModel classe) {
        return new ClasseRecordDto(
                classe.getId(),
                classe.getNome(),
                classe.getValor(),
                classe.getPrazoDevolucao(),
                classe.getTitulos()!= null ? classe.getTitulos().size() : 0
        );
    }
}