package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.TituloRecordDto;
import com.locadora.videolocadora.models.AtorModel;
import com.locadora.videolocadora.models.ClasseModel;
import com.locadora.videolocadora.models.DiretorModel;
import com.locadora.videolocadora.models.TituloModel;
import com.locadora.videolocadora.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TituloService {

    @Autowired
    private TituloRepository tituloRepository;

    @Autowired
    private AtorRepository atorRepository;

    @Autowired
    private DiretorRepository diretorRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<TituloRecordDto> listarTodos() {
        return tituloRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TituloRecordDto> buscarPorId(Long id) {
        return tituloRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<TituloRecordDto> buscarPorNome(String nome) {
        return tituloRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TituloRecordDto> buscarPorCategoria(String categoria) {
        return tituloRepository.findByCategoriaContainingIgnoreCase(categoria).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TituloRecordDto> buscarPorAtor(String nomeAtor) {
        return tituloRepository.findByAtorNome(nomeAtor).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TituloRecordDto criar(TituloRecordDto tituloRecordDto) {
        if (tituloRepository.existsByNome(tituloRecordDto.nome())) {
            throw new RuntimeException("Já existe um título com este nome");
        }

        TituloModel titulo = new TituloModel();
        titulo.setNome(tituloRecordDto.nome());
        titulo.setNomeOriginal(tituloRecordDto.nomeOriginal());
        titulo.setAno(tituloRecordDto.ano());
        titulo.setSinopse(tituloRecordDto.sinopse());
        titulo.setCategoria(tituloRecordDto.categoria());

        // Definir diretor
        DiretorModel diretor = diretorRepository.findById(tituloRecordDto.diretorId())
                .orElseThrow(() -> new RuntimeException("Diretor não encontrado"));
        titulo.setDiretor(diretor);

        // Definir classe
        ClasseModel classe = classeRepository.findById(tituloRecordDto.classeId())
                .orElseThrow(() -> new RuntimeException("Classe não encontrada"));
        titulo.setClasse(classe);

        // Definir atores
        if (tituloRecordDto.atoresIds() != null) {
            Set<AtorModel> atores = new HashSet<>(atorRepository.findAllById(tituloRecordDto.atoresIds()));
            titulo.setAtores(atores);
        }

        TituloModel savedTitulo = tituloRepository.save(titulo);
        return convertToDTO(savedTitulo);
    }

    @Transactional
    public Optional<TituloRecordDto> atualizar(Long id, TituloRecordDto tituloRecordDto) {
        return tituloRepository.findById(id)
                .map(titulo -> {
                    if (!titulo.getNome().equals(tituloRecordDto.nome()) &&
                            tituloRepository.existsByNome(tituloRecordDto.nome())) {
                        throw new RuntimeException("Já existe um título com este nome");
                    }

                    titulo.setNome(tituloRecordDto.nome());
                    titulo.setNomeOriginal(tituloRecordDto.nomeOriginal());
                    titulo.setAno(tituloRecordDto.ano());
                    titulo.setSinopse(tituloRecordDto.sinopse());
                    titulo.setCategoria(tituloRecordDto.categoria());

                    // Atualizar diretor se necessário
                    if (!titulo.getDiretor().getId().equals(tituloRecordDto.diretorId())) {
                        DiretorModel diretor = diretorRepository.findById(tituloRecordDto.diretorId())
                                .orElseThrow(() -> new RuntimeException("Diretor não encontrado"));
                        titulo.setDiretor(diretor);
                    }

                    // Atualizar classe se necessário
                    if (!titulo.getClasse().getId().equals(tituloRecordDto.classeId())) {
                        ClasseModel classe = classeRepository.findById(tituloRecordDto.classeId())
                                .orElseThrow(() -> new RuntimeException("Classe não encontrada"));
                        titulo.setClasse(classe);
                    }

                    // Atualizar atores se necessário
                    if (tituloRecordDto.atoresIds() != null) {
                        Set<AtorModel> atores = new HashSet<>(atorRepository.findAllById(tituloRecordDto.atoresIds()));
                        titulo.setAtores(atores);
                    }

                    TituloModel updatedTitulo = tituloRepository.save(titulo);
                    return convertToDTO(updatedTitulo);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return tituloRepository.findById(id)
                .map(titulo -> {
                    if (!titulo.getItens().isEmpty()) {
                        throw new RuntimeException("Não é possível excluir título que possui itens");
                    }

                    tituloRepository.delete(titulo);
                    return true;
                })
                .orElse(false);
    }

    private TituloRecordDto convertToDTO(TituloModel titulo) {
        return new TituloRecordDto(
                titulo.getId(),
                titulo.getNome(),
                titulo.getNomeOriginal(),
                titulo.getAno(),
                titulo.getSinopse(),
                titulo.getCategoria(),
                titulo.getAtores().stream()
                        .map(AtorModel::getId)
                        .collect(Collectors.toSet()),
                titulo.getDiretor().getId(),
                titulo.getClasse().getId(),
                itemRepository.countAvailableByTituloId(titulo.getId()).intValue()
        );
    }
}