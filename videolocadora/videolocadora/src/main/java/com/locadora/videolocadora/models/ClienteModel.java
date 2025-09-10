package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_inscricao", nullable = false, unique = true)
    private String numInscricao;

    @Column(nullable = false)
    private String nome;

    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dtNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexo sexo;

    @Column(name = "estah_ativo", nullable = false)
    private Boolean estahAtivo;

    @OneToMany(mappedBy = "cliente")
    private Set<LocacaoModel> locacoes = new HashSet<>();

    public enum Sexo {
        MASCULINO, FEMININO
    }

    // Construtores
    public ClienteModel() {
        this.estahAtivo = true;
    }

    public ClienteModel(String numInscricao, String nome, LocalDate dtNascimento, Sexo sexo) {
        this.numInscricao = numInscricao;
        this.nome = nome;
        this.dtNascimento = dtNascimento;
        this.sexo = sexo;
        this.estahAtivo = true;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumInscricao() { return numInscricao; }
    public void setNumInscricao(String numInscricao) { this.numInscricao = numInscricao; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDtNascimento() { return dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento) { this.dtNascimento = dtNascimento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public Boolean getEstahAtivo() { return estahAtivo; }
    public void setEstahAtivo(Boolean estahAtivo) { this.estahAtivo = estahAtivo; }

    public Set<LocacaoModel> getLocacoes() { return locacoes; }
    public void setLocacoes(Set<LocacaoModel> locacoes) { this.locacoes = locacoes; }
}