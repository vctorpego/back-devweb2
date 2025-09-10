package com.locadora.videolocadora.controllers;

import com.locadora.videolocadora.dtos.ClienteRecordDto;
import com.locadora.videolocadora.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteRecordDto> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteRecordDto> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/numero-inscricao/{numInscricao}")
    public ResponseEntity<ClienteRecordDto> buscarPorNumInscricao(@PathVariable String numInscricao) {
        return clienteService.buscarPorNumInscricao(numInscricao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nome")
    public List<ClienteRecordDto> buscarPorNome(@RequestParam String nome) {
        return clienteService.buscarPorNome(nome);
    }

    @GetMapping("/status/{estahAtivo}")
    public List<ClienteRecordDto> buscarPorStatus(@PathVariable Boolean estahAtivo) {
        return clienteService.buscarPorStatus(estahAtivo);
    }

    @GetMapping("/socios")
    public List<ClienteRecordDto> listarSocios() {
        return clienteService.listarSocios();
    }

    @GetMapping("/dependentes")
    public List<ClienteRecordDto> listarDependentes() {
        return clienteService.listarDependentes();
    }
}