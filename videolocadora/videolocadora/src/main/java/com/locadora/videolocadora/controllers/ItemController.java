package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.ItemRecordDto;
import com.locadora.videolocadora.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemRecordDto> listarTodos() {
        return itemService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRecordDto> buscarPorId(@PathVariable Long id) {
        return itemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/serie")
    public ResponseEntity<ItemRecordDto> buscarPorNumeroSerie(@RequestParam String numeroSerie) {
        return itemService.buscarPorNumeroSerie(numeroSerie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponiveis/{tituloId}")
    public List<ItemRecordDto> buscarDisponiveisPorTitulo(@PathVariable Long tituloId) {
        return itemService.buscarDisponiveisPorTitulo(tituloId);
    }

    @PostMapping
    public ResponseEntity<ItemRecordDto> criar(@RequestBody ItemRecordDto itemDTO) {
        try {
            ItemRecordDto savedItem = itemService.criar(itemDTO);
            return ResponseEntity.created(URI.create("/api/itens/" + savedItem.id()))
                    .body(savedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemRecordDto> atualizar(@PathVariable Long id, @RequestBody ItemRecordDto itemDTO) {
        try {
            return itemService.atualizar(id, itemDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (itemService.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/debug/info")
    public ResponseEntity<Map<String, Object>> getDtoInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("dtoClass", "ItemRecordDto");
        info.put("camposEsperados", List.of(
                Map.of("nome", "id", "tipo", "Long", "obrigatorio", false),
                Map.of("nome", "numeroSerie", "tipo", "String", "obrigatorio", true),
                Map.of("nome", "dataAquisicao", "tipo", "LocalDate", "obrigatorio", false, "formato", "yyyy-MM-dd"),
                Map.of("nome", "tipo", "tipo", "TipoItem", "obrigatorio", true, "valores", List.of("FITA", "DVD", "BLURAY")),
                Map.of("nome", "tituloId", "tipo", "Long", "obrigatorio", true),
                Map.of("nome", "tituloNome", "tipo", "String", "obrigatorio", false)
        ));

        Map<String, Object> exemplo = Map.of(
                "numeroSerie", "SN001",
                "dataAquisicao", "2024-01-15",
                "tipo", "DVD",
                "tituloId", 1
        );
        info.put("exemploPayload", exemplo);

        return ResponseEntity.ok(info);
    }
}
