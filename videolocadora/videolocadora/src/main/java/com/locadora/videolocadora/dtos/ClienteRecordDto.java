package com.locadora.videolocadora.dtos;

import com.locadora.videolocadora.models.ClienteModel.Sexo;
import java.time.LocalDate;

public record ClienteRecordDto(
        Long id,
        String numInscricao,
        String nome,
        LocalDate dtNascimento,
        Sexo sexo,
        Boolean estahAtivo,
        String tipoCliente // "SOCIO" ou "DEPENDENTE"
) {}