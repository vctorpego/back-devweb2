package com.locadora.videolocadora.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "locacao")
public class LocacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dt_locacao", nullable = false)
    private LocalDate dtLocacao;

    @Column(name = "dt_devolucao_prevista", nullable = false)
    private LocalDate dtDevolucaoPrevista;

    @Column(name = "dt_devolucao_efetiva")
    private LocalDate dtDevolucaoEfetiva;

    @Column(name = "valor_cobrado", nullable = false)
    private Double valorCobrado;

    @Column(name = "multa_cobrada")
    private Double multaCobrada;

    @Column(name = "estah_paga", nullable = false)
    private Boolean estahPaga;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteModel cliente;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemModel item;

    // Construtores
    public LocacaoModel() {
        this.estahPaga = false;
    }

    public LocacaoModel(ClienteModel cliente, ItemModel item, LocalDate dtLocacao,
                        LocalDate dtDevolucaoPrevista, Double valorCobrado) {
        this.cliente = cliente;
        this.item = item;
        this.dtLocacao = dtLocacao;
        this.dtDevolucaoPrevista = dtDevolucaoPrevista;
        this.valorCobrado = valorCobrado;
        this.estahPaga = false;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDtLocacao() { return dtLocacao; }
    public void setDtLocacao(LocalDate dtLocacao) { this.dtLocacao = dtLocacao; }

    public LocalDate getDtDevolucaoPrevista() { return dtDevolucaoPrevista; }
    public void setDtDevolucaoPrevista(LocalDate dtDevolucaoPrevista) { this.dtDevolucaoPrevista = dtDevolucaoPrevista; }

    public LocalDate getDtDevolucaoEfetiva() { return dtDevolucaoEfetiva; }
    public void setDtDevolucaoEfetiva(LocalDate dtDevolucaoEfetiva) { this.dtDevolucaoEfetiva = dtDevolucaoEfetiva; }

    public Double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(Double valorCobrado) { this.valorCobrado = valorCobrado; }

    public Double getMultaCobrada() { return multaCobrada; }
    public void setMultaCobrada(Double multaCobrada) { this.multaCobrada = multaCobrada; }

    public Boolean getEstahPaga() { return estahPaga; }
    public void setEstahPaga(Boolean estahPaga) { this.estahPaga = estahPaga; }

    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }

    public ItemModel getItem() { return item; }
    public void setItem(ItemModel item) { this.item = item; }

    // Métodos auxiliares
    public boolean estaEmAtraso() {
        LocalDate hoje = LocalDate.now();
        return dtDevolucaoEfetiva == null && hoje.isAfter(dtDevolucaoPrevista);
    }

    public boolean estaVigente() {
        return dtDevolucaoEfetiva == null;
    }

    public long getDiasAtraso() {
        if (dtDevolucaoEfetiva != null && dtDevolucaoEfetiva.isAfter(dtDevolucaoPrevista)) {
            return java.time.temporal.ChronoUnit.DAYS.between(dtDevolucaoPrevista, dtDevolucaoEfetiva);
        }
        return 0;
    }
}