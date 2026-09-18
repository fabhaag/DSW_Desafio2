package com.agenciaviagens.destinos_api.service;

////import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agenciaviagens.destinos_api.dto.DestinoRequestDTO;
import com.agenciaviagens.destinos_api.model.Destino;

// DestinoRepository é uma interface que estende JpaRepository, fornecendo métodos para operações CRUD e consultas personalizadas no banco de dados.
import com.agenciaviagens.destinos_api.repository.DestinoRepository;

@Service
public class DestinoService {

    private final DestinoRepository repository;

    public DestinoService(DestinoRepository repository) {
        this.repository = repository;
    }

    public Destino cadastrar(DestinoRequestDTO dto) {
        
        Destino destino = new Destino(dto.nome(), dto.localizacao(), dto.descricao());
        return repository.save(destino);
    }

    public List<Destino> listarTodos() {
        return repository.findAll();
    }

    public Optional<Destino> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Destino> pesquisar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        
        return repository.findByNomeContainingIgnoreCaseOrLocalizacaoContainingIgnoreCase(termo, termo);
    }

    public Optional<Destino> atualizar(Long id, DestinoRequestDTO dto) {
    
       return repository.findById(id).map(destino -> {
            destino.setNome(dto.nome());
            destino.setLocalizacao(dto.localizacao());
            destino.setDescricao(dto.descricao());
            return repository.save(destino);
        });
    }

    public Optional<Destino> registrarAvaliacao(Long id, int nota) {
        return repository.findById(id).map(destino -> {
            destino.adicionarAvaliacao(nota);
            return repository.save(destino);
        });
    }

    
    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
