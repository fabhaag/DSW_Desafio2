package com.agenciaviagens.destinos_api.controller;

import com.agenciaviagens.destinos_api.dto.AvaliacaoDTO;
import com.agenciaviagens.destinos_api.dto.DestinoRequestDTO;
import com.agenciaviagens.destinos_api.model.Destino;
import com.agenciaviagens.destinos_api.service.DestinoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinos")
public class DestinoController {

    private final DestinoService service;

    public DestinoController(DestinoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Destino> cadastrar(@Valid @RequestBody DestinoRequestDTO dto) {
        Destino novoDestino = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDestino);
    }

    @GetMapping
    public ResponseEntity<List<Destino>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<Destino>> pesquisar(@RequestParam(required = false) String termo) {
        return ResponseEntity.ok(service.pesquisar(termo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> atualizar(@PathVariable Long id, @Valid @RequestBody DestinoRequestDTO dto) {
        return service.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<Destino> avaliar(@PathVariable Long id, @Valid @RequestBody AvaliacaoDTO dto) {
        return service.registrarAvaliacao(id, dto.nota())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (service.excluir(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}