package com.beneficio.backend.mapper;

import com.beneficio.backend.dto.BeneficioRequest;
import com.beneficio.backend.dto.BeneficioResponse;
import com.beneficio.domain.entity.Beneficio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BeneficioMapper {

    // Converte Request para Entidade (Create)
    Beneficio toEntity(BeneficioRequest request);

    // Converte Entidade para Response (Read)
    BeneficioResponse toResponse(Beneficio entity);

    // Atualiza a Entidade existente com os dados do Request (Update)
    void updateEntityFromRequest(BeneficioRequest request, @MappingTarget Beneficio entity);
}