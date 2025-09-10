package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "diretor")
public class DiretorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "diretor")
    private Set<TituloModel> titulos = new HashSet<>();

    public DiretorModel() {}

    public DiretorModel(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Set<TituloModel> getTitulos() { return titulos; }
    public void setTitulos(Set<TituloModel> titulos) { this.titulos = titulos; }
}