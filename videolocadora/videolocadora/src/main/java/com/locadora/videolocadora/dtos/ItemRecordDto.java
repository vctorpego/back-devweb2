package com.locadora.videolocadora.dtos;

import com.locadora.videolocadora.models.ItemModel;
import java.time.LocalDate;

public record ItemRecordDto(
        Long id,
        String numeroSerie,
        LocalDate dataAquisicao,
        ItemModel.TipoItem tipo,
        Long tituloId,
        String tituloNome
) {}
