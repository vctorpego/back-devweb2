package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.DependenteRecordDto;
import com.locadora.videolocadora.services.DependenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dependentes")
public class DependenteController {

    @Autowired
    private DependenteService dependenteService;

    @GetMapping
    public List<DependenteRecordDto> listarTodos() {
        return dependenteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DependenteRecordDto> buscarPorId(@PathVariable Long id) {
        return dependenteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/socio/{socioId}")
    public List<DependenteRecordDto> buscarPorSocioId(@PathVariable Long socioId) {
        return dependenteService.buscarPorSocioId(socioId);
    }

    @GetMapping("/socio/{socioId}/status/{estahAtivo}")
    public List<DependenteRecordDto> buscarPorSocioIdEStatus(@PathVariable Long socioId, @PathVariable Boolean estahAtivo) {
        return dependenteService.buscarPorSocioIdEStatus(socioId, estahAtivo);
    }

    @PostMapping
    public ResponseEntity<DependenteRecordDto> criar(@RequestBody DependenteRecordDto dependenteDto) {
        try {
            DependenteRecordDto savedDependente = dependenteService.criar(dependenteDto);
            return ResponseEntity.created(URI.create("/api/dependentes/" + savedDependente.id()))
                    .body(savedDependente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DependenteRecordDto> atualizar(@PathVariable Long id, @RequestBody DependenteRecordDto dependenteDto) {
        try {
            return dependenteService.atualizar(id, dependenteDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<DependenteRecordDto> desativar(@PathVariable Long id) {
        try {
            return dependenteService.desativar(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<DependenteRecordDto> reativar(@PathVariable Long id) {
        try {
            return dependenteService.reativar(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (dependenteService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}