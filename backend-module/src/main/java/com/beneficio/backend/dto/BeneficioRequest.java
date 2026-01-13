package com.beneficio.backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BeneficioRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor não pode ser zero")
        BigDecimal valor,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean ativo
) {}
