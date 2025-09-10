package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_serie", nullable = false, unique = true)
    private String numeroSerie;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItem tipo;

    @ManyToOne
    @JoinColumn(name = "titulo_id", nullable = false)
    private TituloModel titulo;

    @OneToMany(mappedBy = "item")
    private Set<LocacaoModel> locacoes = new HashSet<>();

    public enum TipoItem {
        FITA, DVD, BLURAY
    }

    // Construtores
    public ItemModel() {}

    public ItemModel(String numeroSerie, TituloModel titulo, TipoItem tipo) {
        this.numeroSerie = numeroSerie;
        this.titulo = titulo;
        this.tipo = tipo;
        this.dataAquisicao = LocalDate.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public LocalDate getDataAquisicao() { return dataAquisicao; }
    public void setDataAquisicao(LocalDate dataAquisicao) { this.dataAquisicao = dataAquisicao; }

    public TipoItem getTipo() { return tipo; }
    public void setTipo(TipoItem tipo) { this.tipo = tipo; }

    public TituloModel getTitulo() { return titulo; }
    public void setTitulo(TituloModel titulo) { this.titulo = titulo; }

    public Set<LocacaoModel> getLocacoes() { return locacoes; }
    public void setLocacoes(Set<LocacaoModel> locacoes) { this.locacoes = locacoes; }
}