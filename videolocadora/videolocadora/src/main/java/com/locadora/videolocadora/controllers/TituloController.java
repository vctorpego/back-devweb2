package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.TituloRecordDto;
import com.locadora.videolocadora.services.TituloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/titulos")
public class TituloController {

    @Autowired
    private TituloService tituloService;

    @GetMapping
    public List<TituloRecordDto> listarTodos() {
        return tituloService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TituloRecordDto> buscarPorId(@PathVariable Long id) {
        return tituloService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nome")
    public List<TituloRecordDto> buscarPorNome(@RequestParam String nome) {
        return tituloService.buscarPorNome(nome);
    }

    @GetMapping("/buscar/categoria")
    public List<TituloRecordDto> buscarPorCategoria(@RequestParam String categoria) {
        return tituloService.buscarPorCategoria(categoria);
    }

    @GetMapping("/buscar/ator")
    public List<TituloRecordDto> buscarPorAtor(@RequestParam String nomeAtor) {
        return tituloService.buscarPorAtor(nomeAtor);
    }

    @PostMapping
    public ResponseEntity<TituloRecordDto> criar(@RequestBody TituloRecordDto tituloDTO) {
        try {
            TituloRecordDto savedTitulo = tituloService.criar(tituloDTO);
            return ResponseEntity.created(URI.create("/api/titulos/" + savedTitulo.id()))
                    .body(savedTitulo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TituloRecordDto> atualizar(@PathVariable Long id, @RequestBody TituloRecordDto tituloDTO) {
        try {
            return tituloService.atualizar(id, tituloDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (tituloService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
