package com.locadora.videolocadora.dtos;

import java.time.LocalDate;

public record LocacaoRecordDto(
        Long id,
        LocalDate dtLocacao,
        LocalDate dtDevolucaoPrevista,
        LocalDate dtDevolucaoEfetiva,
        Double valorCobrado,
        Double multaCobrada,
        Boolean estahPaga,
        Long clienteId,
        String clienteNome,
        Long itemId,
        String itemNumeroSerie,
        Long tituloId,
        String tituloNome,
        Boolean estaEmAtraso,
        Boolean estaVigente,
        Long diasAtraso
) {}