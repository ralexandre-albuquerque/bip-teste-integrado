package com.beneficio.backend.controller;

import com.beneficio.backend.dto.BeneficioFilter;
import com.beneficio.backend.dto.BeneficioRequest;
import com.beneficio.backend.dto.BeneficioResponse;
import com.beneficio.backend.dto.PagedModel;
import com.beneficio.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    @Operation(summary = "Listar benefícios", description = "Retorna uma lista paginada de benefícios com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de benefícios retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Page.class)))
    public ResponseEntity<PagedModel<BeneficioResponse>> list(
            @ParameterObject BeneficioFilter filter,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<BeneficioResponse> page = beneficioService.findAll(filter, pageable);
        return ResponseEntity.ok(PagedModel.from(page));
    }

    @PostMapping
    @Operation(summary = "Criar um novo benefício")
    @ApiResponse(responseCode = "201", description = "Benefício criado",
            content = @Content(schema = @Schema(implementation = BeneficioResponse.class)))
// As respostas 400 e 500 virão automaticamente da configuração global
    public ResponseEntity<BeneficioResponse> create(@Valid @RequestBody BeneficioRequest beneficioRequest) {
        BeneficioResponse novoBeneficio = beneficioService.create(beneficioRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoBeneficio.id())
                .toUri();

        return ResponseEntity.created(uri).body(novoBeneficio);
    }
}
