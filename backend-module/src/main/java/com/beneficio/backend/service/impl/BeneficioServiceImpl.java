package com.beneficio.backend.service.impl;

import com.beneficio.backend.dto.BeneficioFilter;
import com.beneficio.backend.dto.BeneficioRequest;
import com.beneficio.backend.dto.BeneficioResponse;
import com.beneficio.backend.exception.BusinessException;
import com.beneficio.backend.exception.ResourceNotFoundException;
import com.beneficio.backend.mapper.BeneficioMapper;
import com.beneficio.backend.repository.BeneficioRepository;
import com.beneficio.backend.repository.specification.BeneficioSpecification;
import com.beneficio.backend.service.BeneficioService;
import com.beneficio.domain.entity.Beneficio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeneficioServiceImpl implements BeneficioService {

    private final BeneficioRepository repository;
    private final BeneficioMapper mapper;

    public BeneficioServiceImpl(BeneficioRepository repository, BeneficioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficioResponse> findAll(BeneficioFilter filter, Pageable pageable) {
        return repository.findAll(BeneficioSpecification.filterBy(filter), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public BeneficioResponse create(BeneficioRequest request) {
        if (repository.existsByNomeIgnoreCase(request.nome())) {
            throw new BusinessException("Já existe um benefício cadastrado com o nome: " + request.nome());
        }

        Beneficio beneficio = mapper.toEntity(request);
        beneficio = repository.save(beneficio);
        return mapper.toResponse(beneficio);
    }

    @Override
    @Transactional
    public BeneficioResponse update(Long id, BeneficioRequest request) {
        Beneficio beneficio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benefício não encontrado com o ID: " + id));

        if (!beneficio.getNome().equalsIgnoreCase(request.nome()) &&
                repository.existsByNomeIgnoreCase(request.nome())) {
            throw new BusinessException("Já existe outro benefício cadastrado com o nome: " + request.nome());
        }

        mapper.updateEntityFromRequest(request, beneficio);

        beneficio = repository.save(beneficio);
        return mapper.toResponse(beneficio);
    }
}
