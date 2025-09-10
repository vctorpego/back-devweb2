package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classe")
public class ClasseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "prazo_devolucao", nullable = false)
    private Integer prazoDevolucao;

    @OneToMany(mappedBy = "classe")
    private Set<TituloModel> titulos = new HashSet<>();

    public ClasseModel() {}

    public ClasseModel(String nome, Double valor, Integer prazoDevolucao) {
        this.nome = nome;
        this.valor = valor;
        this.prazoDevolucao = prazoDevolucao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Integer getPrazoDevolucao() { return prazoDevolucao; }
    public void setPrazoDevolucao(Integer prazoDevolucao) { this.prazoDevolucao = prazoDevolucao; }

    public Set<TituloModel> getTitulos() { return titulos; }
    public void setTitulos(Set<TituloModel> titulos) { this.titulos = titulos; }
}