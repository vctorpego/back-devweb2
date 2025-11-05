package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.LocacaoRecordDto;
import com.locadora.videolocadora.services.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

    @Autowired
    private LocacaoService locacaoService;

    @GetMapping
    public List<LocacaoRecordDto> listarTodos() {
        return locacaoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocacaoRecordDto> buscarPorId(@PathVariable Long id) {
        return locacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<LocacaoRecordDto> buscarPorClienteId(@PathVariable Long clienteId) {
        return locacaoService.buscarPorClienteId(clienteId);
    }

    @GetMapping("/cliente/{clienteId}/vigentes")
    public List<LocacaoRecordDto> buscarLocacoesVigentesPorClienteId(@PathVariable Long clienteId) {
        return locacaoService.buscarLocacoesVigentesPorClienteId(clienteId);
    }

    @GetMapping("/atraso")
    public List<LocacaoRecordDto> buscarLocacoesEmAtraso() {
        return locacaoService.buscarLocacoesEmAtraso();
    }

    @GetMapping("/cliente/{clienteId}/atraso")
    public List<LocacaoRecordDto> buscarLocacoesEmAtrasoPorClienteId(@PathVariable Long clienteId) {
        return locacaoService.buscarLocacoesEmAtrasoPorClienteId(clienteId);
    }

    @PostMapping
    public ResponseEntity<LocacaoRecordDto> criarLocacao(@RequestBody LocacaoRecordDto locacaoDto) {
        try {
            LocacaoRecordDto savedLocacao = locacaoService.criarLocacao(locacaoDto);
            return ResponseEntity.created(URI.create("/api/locacoes/" + savedLocacao.id()))
                    .body(savedLocacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocacaoRecordDto> atualizarLocacao(@PathVariable Long id, @RequestBody LocacaoRecordDto locacaoDto) {
        try {
            return locacaoService.atualizarLocacao(id, locacaoDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<LocacaoRecordDto> efetuarDevolucao(@PathVariable Long id) {
        try {
            return locacaoService.efetuarDevolucao(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/pagamento")
    public ResponseEntity<LocacaoRecordDto> efetuarPagamento(@PathVariable Long id) {
        try {
            return locacaoService.efetuarPagamento(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarLocacao(@PathVariable Long id) {
        try {
            if (locacaoService.cancelarLocacao(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}