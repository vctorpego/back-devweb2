package com.locadora.videolocadora.services;

import com.locadora.videolocadora.dtos.LocacaoRecordDto;
import com.locadora.videolocadora.models.*;
import com.locadora.videolocadora.repositories.ClienteRepository;
import com.locadora.videolocadora.repositories.ItemRepository;
import com.locadora.videolocadora.repositories.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<LocacaoRecordDto> listarTodos() {
        return locacaoRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<LocacaoRecordDto> buscarPorId(Long id) {
        return locacaoRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<LocacaoRecordDto> buscarPorClienteId(Long clienteId) {
        return locacaoRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<LocacaoRecordDto> buscarLocacoesVigentesPorClienteId(Long clienteId) {
        return locacaoRepository.findLocacoesVigentesByClienteId(clienteId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<LocacaoRecordDto> buscarLocacoesEmAtraso() {
        return locacaoRepository.findLocacoesEmAtraso(LocalDate.now()).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<LocacaoRecordDto> buscarLocacoesEmAtrasoPorClienteId(Long clienteId) {
        return locacaoRepository.findLocacoesEmAtrasoByClienteId(clienteId, LocalDate.now()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public LocacaoRecordDto criarLocacao(LocacaoRecordDto locacaoDto) {
        ClienteModel cliente = clienteRepository.findById(locacaoDto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        ItemModel item = itemRepository.findById(locacaoDto.itemId())
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        // Verificar se o cliente está em débito
        List<LocacaoModel> locacoesEmAtraso = locacaoRepository.findLocacoesEmAtrasoByClienteId(cliente.getId(), LocalDate.now());
        if (!locacoesEmAtraso.isEmpty()) {
            throw new RuntimeException("Cliente está em débito com locações em atraso");
        }

        // Verificar se o item já está locado
        Optional<LocacaoModel> locacaoVigente = locacaoRepository.findLocacaoVigenteByItemId(item.getId());
        if (locacaoVigente.isPresent()) {
            throw new RuntimeException("Item já está locado");
        }

        // Verificar se o item está disponível
        if (!itemRepository.findAvailableByTituloId(item.getTitulo().getId()).contains(item)) {
            throw new RuntimeException("Item não está disponível");
        }

        LocacaoModel locacao = new LocacaoModel();
        locacao.setCliente(cliente);
        locacao.setItem(item);
        locacao.setDtLocacao(locacaoDto.dtLocacao() != null ? locacaoDto.dtLocacao() : LocalDate.now());
        locacao.setDtDevolucaoPrevista(locacaoDto.dtDevolucaoPrevista());
        locacao.setValorCobrado(locacaoDto.valorCobrado());
        locacao.setEstahPaga(locacaoDto.estahPaga() != null ? locacaoDto.estahPaga() : false);

        LocacaoModel savedLocacao = locacaoRepository.save(locacao);
        return convertToDto(savedLocacao);
    }

    @Transactional
    public Optional<LocacaoRecordDto> atualizarLocacao(Long id, LocacaoRecordDto locacaoDto) {
        return locacaoRepository.findById(id)
                .map(locacao -> {
                    if (locacaoDto.dtLocacao() != null) {
                        locacao.setDtLocacao(locacaoDto.dtLocacao());
                    }
                    if (locacaoDto.dtDevolucaoPrevista() != null) {
                        locacao.setDtDevolucaoPrevista(locacaoDto.dtDevolucaoPrevista());
                    }
                    if (locacaoDto.valorCobrado() != null) {
                        locacao.setValorCobrado(locacaoDto.valorCobrado());
                    }
                    if (locacaoDto.estahPaga() != null) {
                        locacao.setEstahPaga(locacaoDto.estahPaga());
                    }

                    LocacaoModel updatedLocacao = locacaoRepository.save(locacao);
                    return convertToDto(updatedLocacao);
                });
    }

    @Transactional
    public Optional<LocacaoRecordDto> efetuarDevolucao(Long locacaoId) {
        return locacaoRepository.findById(locacaoId)
                .map(locacao -> {
                    if (locacao.getDtDevolucaoEfetiva() != null) {
                        throw new RuntimeException("Locação já foi devolvida");
                    }

                    locacao.setDtDevolucaoEfetiva(LocalDate.now());

                    // Calcular multa se houver atraso
                    if (locacao.estaEmAtraso()) {
                        long diasAtraso = locacao.getDiasAtraso();
                        // Exemplo: multa de 2 reais por dia de atraso
                        double multa = diasAtraso * 2.0;
                        locacao.setMultaCobrada(multa);
                    }

                    LocacaoModel updatedLocacao = locacaoRepository.save(locacao);
                    return convertToDto(updatedLocacao);
                });
    }

    @Transactional
    public Optional<LocacaoRecordDto> efetuarPagamento(Long locacaoId) {
        return locacaoRepository.findById(locacaoId)
                .map(locacao -> {
                    locacao.setEstahPaga(true);
                    LocacaoModel updatedLocacao = locacaoRepository.save(locacao);
                    return convertToDto(updatedLocacao);
                });
    }

    @Transactional
    public boolean cancelarLocacao(Long id) {
        return locacaoRepository.findById(id)
                .map(locacao -> {
                    if (locacao.getEstahPaga()) {
                        throw new RuntimeException("Não é possível cancelar locação paga");
                    }
                    if (locacao.getDtDevolucaoEfetiva() != null) {
                        throw new RuntimeException("Não é possível cancelar locação já devolvida");
                    }

                    locacaoRepository.delete(locacao);
                    return true;
                })
                .orElse(false);
    }

    private LocacaoRecordDto convertToDto(LocacaoModel locacao) {
        return new LocacaoRecordDto(
                locacao.getId(),
                locacao.getDtLocacao(),
                locacao.getDtDevolucaoPrevista(),
                locacao.getDtDevolucaoEfetiva(),
                locacao.getValorCobrado(),
                locacao.getMultaCobrada(),
                locacao.getEstahPaga(),
                locacao.getCliente().getId(),
                locacao.getCliente().getNome(),
                locacao.getItem().getId(),
                locacao.getItem().getNumeroSerie(),
                locacao.getItem().getTitulo().getId(),
                locacao.getItem().getTitulo().getNome(),
                locacao.estaEmAtraso(),
                locacao.estaVigente(),
                locacao.getDiasAtraso()
        );
    }
}