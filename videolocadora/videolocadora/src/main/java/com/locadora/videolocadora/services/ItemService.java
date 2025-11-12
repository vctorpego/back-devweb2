package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.ItemRecordDto;
import com.locadora.videolocadora.models.ItemModel;
import com.locadora.videolocadora.models.TituloModel;
import com.locadora.videolocadora.repositories.ItemRepository;
import com.locadora.videolocadora.repositories.TituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TituloRepository tituloRepository;

    public List<ItemRecordDto> listarTodos() {
        return itemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ItemRecordDto> buscarPorId(Long id) {
        return itemRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<ItemRecordDto> buscarPorNumeroSerie(String numeroSerie) {
        return itemRepository.findByNumeroSerie(numeroSerie)
                .map(this::convertToDTO);
    }

    public List<ItemRecordDto> buscarDisponiveisPorTitulo(Long tituloId) {
        return itemRepository.findAvailableByTituloId(tituloId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemRecordDto criar(ItemRecordDto itemRecordDto) {
        if (itemRepository.existsByNumeroSerie(itemRecordDto.numeroSerie())) {
            throw new RuntimeException("Já existe um item com este número de série");
        }

        TituloModel titulo = tituloRepository.findById(itemRecordDto.tituloId())
                .orElseThrow(() -> new RuntimeException("Título não encontrado"));

        ItemModel item = new ItemModel();
        item.setNumeroSerie(itemRecordDto.numeroSerie());
        item.setDataAquisicao(itemRecordDto.dataAquisicao());
        item.setTipo(itemRecordDto.tipo());
        item.setTitulo(titulo);

        ItemModel savedItem = itemRepository.save(item);
        return convertToDTO(savedItem);
    }

    @Transactional
    public Optional<ItemRecordDto> atualizar(Long id, ItemRecordDto itemRecordDto) {
        return itemRepository.findById(id)
                .map(item -> {
                    if (!item.getNumeroSerie().equals(itemRecordDto.numeroSerie()) &&
                            itemRepository.existsByNumeroSerie(itemRecordDto.numeroSerie())) {
                        throw new RuntimeException("Já existe um item com este número de série");
                    }

                    item.setNumeroSerie(itemRecordDto.numeroSerie());
                    item.setTipo(itemRecordDto.tipo());
                    item.setDataAquisicao(itemRecordDto.dataAquisicao());

                    // Atualizar título se necessário
                    if (!item.getTitulo().getId().equals(itemRecordDto.tituloId())) {
                        TituloModel titulo = tituloRepository.findById(itemRecordDto.tituloId())
                                .orElseThrow(() -> new RuntimeException("Título não encontrado"));
                        item.setTitulo(titulo);
                    }

                    ItemModel updatedItem = itemRepository.save(item);
                    return convertToDTO(updatedItem);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    // Verificar se o item está alugado
                    if (!item.getLocacoes().isEmpty()) {
                        boolean isAlugado = item.getLocacoes().stream()
                                .anyMatch(locacao -> locacao.getDtDevolucaoEfetiva() == null);

                        if (isAlugado) {
                            throw new RuntimeException("Não é possível excluir item que está alugado");
                        }
                    }

                    itemRepository.delete(item);
                    return true;
                })
                .orElse(false);
    }

    private ItemRecordDto convertToDTO(ItemModel item) {
        return new ItemRecordDto(
                item.getId(),
                item.getNumeroSerie(),
                item.getDataAquisicao(),
                item.getTipo(),
                item.getTitulo().getId(),
                item.getTitulo().getNome()
        );
    }
}