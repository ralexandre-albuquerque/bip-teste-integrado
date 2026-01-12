package com.beneficio.backend.mapper;

import com.beneficio.backend.dto.BeneficioResponse;
import com.beneficio.domain.entity.Beneficio;
import org.springframework.stereotype.Component;

@Component
public class BeneficioMapper {
    public BeneficioResponse toResponse(Beneficio entity) {
        if (entity == null) return null;
        return new BeneficioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getAtivo()
        );
    }
}
