package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.SocioRecordDto;
import com.locadora.videolocadora.services.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioController {

    @Autowired
    private SocioService socioService;

    @GetMapping
    public List<SocioRecordDto> listarTodos() {
        return socioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioRecordDto> buscarPorId(@PathVariable Long id) {
        return socioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/cpf/{cpf}")
    public ResponseEntity<SocioRecordDto> buscarPorCpf(@PathVariable String cpf) {
        return socioService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nome")
    public List<SocioRecordDto> buscarPorNome(@RequestParam String nome) {
        return socioService.buscarPorNome(nome);
    }

    @GetMapping("/status/{estahAtivo}")
    public List<SocioRecordDto> buscarPorStatus(@PathVariable Boolean estahAtivo) {
        return socioService.buscarPorStatus(estahAtivo);
    }

    @GetMapping("/com-vagas-dependentes")
    public List<SocioRecordDto> buscarSociosComVagasParaDependentes() {
        return socioService.buscarSociosComVagasParaDependentes();
    }

    @PostMapping
    public ResponseEntity<SocioRecordDto> criar(@RequestBody SocioRecordDto socioDto) {
        try {
            SocioRecordDto savedSocio = socioService.criar(socioDto);
            return ResponseEntity.created(URI.create("/api/socios/" + savedSocio.id()))
                    .body(savedSocio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioRecordDto> atualizar(@PathVariable Long id, @RequestBody SocioRecordDto socioDto) {
        try {
            return socioService.atualizar(id, socioDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<SocioRecordDto> desativar(@PathVariable Long id) {
        try {
            return socioService.desativar(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<SocioRecordDto> reativar(@PathVariable Long id) {
        try {
            return socioService.reativar(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (socioService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}