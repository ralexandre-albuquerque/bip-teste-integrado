package com.beneficio.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO padronizado para respostas de erro da API.
 */
public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> details
) {}