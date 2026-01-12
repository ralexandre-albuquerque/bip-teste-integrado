package com.beneficio.backend.dto;

import java.math.BigDecimal;

public record BeneficioFilter(
        String nome,
        Boolean ativo,
        BigDecimal valorMin,
        BigDecimal valorMax
) {}