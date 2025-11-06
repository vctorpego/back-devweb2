package com.locadora.videolocadora.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.locadora.videolocadora.models.ItemModel;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemRecordDto(
        Long id,
        String numeroSerie,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataAquisicao,
        ItemModel.TipoItem tipo,
        Long tituloId,
        String tituloNome
) {}
