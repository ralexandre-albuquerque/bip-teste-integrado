package com.beneficio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Envelope de paginação padronizado")
public record PagedModel<T>(
    @Schema(description = "Lista de itens retornados")
    List<T> content,
    
    @Schema(description = "Metadados de navegação")
    PageMetadata pageMeta
) {
    public record PageMetadata(
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize
    ) {}

    public static <T> PagedModel<T> from(Page<T> page) {
        return new PagedModel<>(
            page.getContent(),
            new PageMetadata(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
            )
        );
    }
}