package com.agenciaviagens.destinos_api.model;

import java.util.Objects;

public class Destino {
    private Long id;
    private String nome;
    private String localizacao;
    private String descricao;
    private Double mediaAvaliacao;
    private Integer totalAvaliacoes;

    public Destino() {
        this.mediaAvaliacao = 0.0;
        this.totalAvaliacoes = 0;
    }

    public Destino(Long id, String nome, String localizacao, String descricao) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.mediaAvaliacao = 0.0;
        this.totalAvaliacoes = 0;
    }

    public void adicionarAvaliacao(int nota) {
        double somaAtual = this.mediaAvaliacao * this.totalAvaliacoes;
        this.totalAvaliacoes++;
        this.mediaAvaliacao = (somaAtual + nota) / this.totalAvaliacoes;
        this.mediaAvaliacao = Math.round(this.mediaAvaliacao * 100.0) / 100.0;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getMediaAvaliacao() { return mediaAvaliacao; }
    public Integer getTotalAvaliacoes() { return totalAvaliacoes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destino destino = (Destino) o;
        return Objects.equals(id, destino.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}