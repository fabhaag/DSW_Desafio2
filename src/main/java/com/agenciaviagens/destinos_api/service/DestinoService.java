package com.agenciaviagens.destinos_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agenciaviagens.destinos_api.dto.DestinoRequestDTO;
import com.agenciaviagens.destinos_api.model.Destino;

@Service
public class DestinoService {

    private final Map<Long, Destino> repositorio = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Destino cadastrar(DestinoRequestDTO dto) {
        Long id = sequence.incrementAndGet();
        Destino destino = new Destino(id, dto.nome(), dto.localizacao(), dto.descricao());
        repositorio.put(id, destino);
        return destino;
    }

    public List<Destino> listarTodos() {
        return new ArrayList<>(repositorio.values());
    }

    public Optional<Destino> buscarPorId(Long id) {
        return Optional.ofNullable(repositorio.get(id));
    }

    public List<Destino> pesquisar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        String termoNormalizado = termo.toLowerCase().trim();
        return repositorio.values().stream()
                .filter(d -> d.getNome().toLowerCase().contains(termoNormalizado) ||
                             d.getLocalizacao().toLowerCase().contains(termoNormalizado))
                .collect(Collectors.toList());
    }

    public Optional<Destino> atualizar(Long id, DestinoRequestDTO dto) {
        Destino destino = repositorio.get(id);
        if (destino != null) {
            destino.setNome(dto.nome());
            destino.setLocalizacao(dto.localizacao());
            destino.setDescricao(dto.descricao());
            return Optional.of(destino);
        }
        return Optional.empty();
    }

    public Optional<Destino> registrarAvaliacao(Long id, int nota) {
        Destino destino = repositorio.get(id);
        if (destino != null) {
            destino.adicionarAvaliacao(nota);
            return Optional.of(destino);
        }
        return Optional.empty();
    }

    public boolean excluir(Long id) {
        return repositorio.remove(id) != null;
    }
}