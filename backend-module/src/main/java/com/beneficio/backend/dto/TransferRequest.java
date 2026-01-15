package com.beneficio.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "ID de origem é obrigatório")
        Long fromId,

        @NotNull(message = "ID de destino é obrigatório")
        Long toId,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount
) {}