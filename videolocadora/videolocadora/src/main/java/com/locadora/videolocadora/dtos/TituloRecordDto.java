package com.locadora.videolocadora.dtos;

import java.util.Set;

public record TituloRecordDto(
        Long id,
        String nome,
        String nomeOriginal,
        Integer ano,
        String sinopse,
        String categoria,
        Set<Long> atoresIds,
        Long diretorId,
        Long classeId,
        Integer quantidadeItensDisponiveis
) {}