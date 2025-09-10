package com.locadora.videolocadora.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "socio")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class SocioModel extends ClienteModel {

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String endereco;

    private String telefone;

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DependenteModel> dependentes = new HashSet<>();

    // Construtores
    public SocioModel() {}

    public SocioModel(String numInscricao, String nome, LocalDate dtNascimento,
                      Sexo sexo, String cpf, String endereco, String telefone) {
        super(numInscricao, nome, dtNascimento, sexo);
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Set<DependenteModel> getDependentes() { return dependentes; }
    public void setDependentes(Set<DependenteModel> dependentes) { this.dependentes = dependentes; }

    public long getQuantidadeDependentesAtivos() {
        return dependentes.stream()
                .filter(DependenteModel::getEstahAtivo)
                .count();
    }
}