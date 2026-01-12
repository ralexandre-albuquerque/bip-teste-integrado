package com.beneficio.backend.service;

import com.beneficio.backend.dto.BeneficioFilter;
import com.beneficio.backend.dto.BeneficioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeneficioService {

    Page<BeneficioResponse> findAll(BeneficioFilter filter, Pageable pageable);
}
