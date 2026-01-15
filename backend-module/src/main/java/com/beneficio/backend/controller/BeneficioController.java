package com.beneficio.backend.controller;

import com.beneficio.backend.controller.openapi.BeneficioControllerOpenApi;
import com.beneficio.backend.dto.*;
import com.beneficio.backend.service.BeneficioService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController implements BeneficioControllerOpenApi {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<BeneficioResponse>> list(
            @ParameterObject BeneficioFilter filter,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<BeneficioResponse> page = beneficioService.findAll(filter, pageable);
        return ResponseEntity.ok(PagedModel.from(page));
    }

    @PostMapping
    public ResponseEntity<BeneficioResponse> create(@Valid @RequestBody BeneficioRequest beneficioRequest) {
        BeneficioResponse novoBeneficio = beneficioService.create(beneficioRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoBeneficio.id())
                .toUri();

        return ResponseEntity.created(uri).body(novoBeneficio);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody BeneficioRequest request) {

        BeneficioResponse response = beneficioService.update(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        beneficioService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/transferencias")
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody TransferRequest request) {

        beneficioService.transfer(request);
        return ResponseEntity.ok().build();
    }
}
