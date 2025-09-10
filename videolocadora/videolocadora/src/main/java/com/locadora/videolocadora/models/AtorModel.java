package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ator")
public class AtorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "atores")
    private Set<TituloModel> titulos = new HashSet<>();

    public AtorModel() {}

    public AtorModel(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Set<TituloModel> getTitulos() { return titulos; }
    public void setTitulos(Set<TituloModel> titulos) { this.titulos = titulos; }
}