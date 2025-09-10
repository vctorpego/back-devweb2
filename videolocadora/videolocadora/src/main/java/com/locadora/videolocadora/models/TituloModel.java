package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "titulo")
public class TituloModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "nome_original")
    private String nomeOriginal;

    private Integer ano;

    private String sinopse;

    private String categoria;

    @ManyToMany
    @JoinTable(
            name = "titulo_ator",
            joinColumns = @JoinColumn(name = "titulo_id"),
            inverseJoinColumns = @JoinColumn(name = "ator_id")
    )
    private Set<AtorModel> atores = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "diretor_id")
    private DiretorModel diretor;

    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private ClasseModel classe;

    @OneToMany(mappedBy = "titulo")
    private Set<ItemModel> itens = new HashSet<>();

    public TituloModel() {}

    public TituloModel(String nome, DiretorModel diretor, ClasseModel classe) {
        this.nome = nome;
        this.diretor = diretor;
        this.classe = classe;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNomeOriginal() { return nomeOriginal; }
    public void setNomeOriginal(String nomeOriginal) { this.nomeOriginal = nomeOriginal; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Set<AtorModel> getAtores() { return atores; }
    public void setAtores(Set<AtorModel> atores) { this.atores = atores; }

    public DiretorModel getDiretor() { return diretor; }
    public void setDiretor(DiretorModel diretor) { this.diretor = diretor; }

    public ClasseModel getClasse() { return classe; }
    public void setClasse(ClasseModel classe) { this.classe = classe; }

    public Set<ItemModel> getItens() { return itens; }
    public void setItens(Set<ItemModel> itens) { this.itens = itens; }
}