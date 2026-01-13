package com.beneficio.backend.service;

import com.beneficio.backend.dto.BeneficioFilter;
import com.beneficio.backend.dto.BeneficioRequest;
import com.beneficio.backend.dto.BeneficioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeneficioService {

    Page<BeneficioResponse> findAll(BeneficioFilter filter, Pageable pageable);

    BeneficioResponse create(BeneficioRequest beneficioRequest);

    BeneficioResponse update(Long id, BeneficioRequest request);
}
