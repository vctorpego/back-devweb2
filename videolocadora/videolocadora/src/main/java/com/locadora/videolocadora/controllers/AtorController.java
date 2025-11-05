package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.AtorRecordDto;
import com.locadora.videolocadora.services.AtorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/atores")
public class AtorController {

    @Autowired
    private AtorService atorService;

    @GetMapping
    public List<AtorRecordDto> listarTodos() {
        return atorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtorRecordDto> buscarPorId(@PathVariable Long id) {
        return atorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<AtorRecordDto> buscarPorNome(@RequestParam String nome) {
        return atorService.buscarPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<AtorRecordDto> criar(@RequestBody AtorRecordDto atorDto) {
        try {
            AtorRecordDto savedAtor = atorService.criar(atorDto);
            return ResponseEntity.created(URI.create("/api/atores/" + savedAtor.id()))
                    .body(savedAtor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtorRecordDto> atualizar(@PathVariable Long id, @RequestBody AtorRecordDto atorDTO) {
        try {
            return atorService.atualizar(id, atorDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (atorService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
