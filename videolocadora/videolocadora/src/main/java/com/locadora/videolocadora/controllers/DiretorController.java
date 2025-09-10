package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.DiretorRecordDto;
import com.locadora.videolocadora.services.DiretorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/diretores")
public class DiretorController {

    @Autowired
    private DiretorService diretorService;

    @GetMapping
    public List<DiretorRecordDto> listarTodos() {
        return diretorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiretorRecordDto> buscarPorId(@PathVariable Long id) {
        return diretorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<DiretorRecordDto> buscarPorNome(@RequestParam String nome) {
        return diretorService.buscarPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<DiretorRecordDto> criar(@RequestBody DiretorRecordDto diretorDTO) {
        try {
            DiretorRecordDto savedDiretor = diretorService.criar(diretorDTO);
            return ResponseEntity.created(URI.create("/api/diretores/" + savedDiretor.id()))
                    .body(savedDiretor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiretorRecordDto> atualizar(@PathVariable Long id, @RequestBody DiretorRecordDto diretorDTO) {
        try {
            return diretorService.atualizar(id, diretorDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (diretorService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}