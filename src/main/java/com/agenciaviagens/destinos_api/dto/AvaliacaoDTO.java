package com.agenciaviagens.destinos_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoDTO(
    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima permitida é 1")
    @Max(value = 5, message = "A nota máxima permitida é 5")
    Integer nota
) {}