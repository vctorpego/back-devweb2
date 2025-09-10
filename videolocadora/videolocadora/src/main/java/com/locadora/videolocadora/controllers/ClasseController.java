package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.ClasseRecordDto;
import com.locadora.videolocadora.services.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @GetMapping
    public List<ClasseRecordDto> listarTodos() {
        return classeService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasseRecordDto> buscarPorId(@PathVariable Long id) {
        return classeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<ClasseRecordDto> buscarPorNome(@RequestParam String nome) {
        return classeService.buscarPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<ClasseRecordDto> criar(@RequestBody ClasseRecordDto classeDTO) {
        try {
            ClasseRecordDto savedClasse = classeService.criar(classeDTO);
            return ResponseEntity.created(URI.create("/api/classes/" + savedClasse.id()))
                    .body(savedClasse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClasseRecordDto> atualizar(@PathVariable Long id, @RequestBody ClasseRecordDto classeDTO) {
        try {
            return classeService.atualizar(id, classeDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (classeService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
