package com.agenciaviagens.destinos_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DestinoRequestDTO(
    @NotBlank(message = "O nome do destino é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String nome,

    @NotBlank(message = "A localização é obrigatória")
    String localizacao,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres")
    String descricao
) {}