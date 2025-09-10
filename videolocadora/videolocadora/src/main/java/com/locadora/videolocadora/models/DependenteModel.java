package com.locadora.videolocadora.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "dependente")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class DependenteModel extends ClienteModel {

    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private SocioModel socio;

    // Construtores
    public DependenteModel() {}

    public DependenteModel(String numInscricao, String nome, LocalDate dtNascimento,
                           Sexo sexo, SocioModel socio) {
        super(numInscricao, nome, dtNascimento, sexo);
        this.socio = socio;
    }

    // Getters e Setters
    public SocioModel getSocio() { return socio; }
    public void setSocio(SocioModel socio) { this.socio = socio; }
}