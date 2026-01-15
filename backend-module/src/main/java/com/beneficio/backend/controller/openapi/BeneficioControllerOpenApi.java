package com.beneficio.backend.controller.openapi;

import com.beneficio.backend.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

@Tag(name = "Benefícios", description = "Endpoints para gestão e transferência de benefícios")
public interface BeneficioControllerOpenApi {

    @Operation(summary = "Listar benefícios", description = "Retorna uma lista paginada de benefícios com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de benefícios retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Page.class)))
    ResponseEntity<PagedModel<BeneficioResponse>> list(
            @ParameterObject BeneficioFilter filter,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable);

    @Operation(summary = "Criar um novo benefício")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Benefício criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome duplicado")
    })
    ResponseEntity<BeneficioResponse> create(@Valid BeneficioRequest beneficioRequest);


    @Operation(summary = "Atualizar um benefício", description = "Atualiza os dados de um benefício existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada")
    })
    ResponseEntity<BeneficioResponse> update(Long id, @Valid BeneficioRequest request);

    @Operation(summary = "Excluir um benefício")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Benefício excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    ResponseEntity<Void> delete(Long id);

    @Operation(summary = "Transferir valor entre benefícios", description = "Realiza a transferência de valor entre dois benefícios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada"),
            @ApiResponse(responseCode = "404", description = "Benefício de origem ou destino não encontrado")
    })
    ResponseEntity<Void> transfer(@Valid TransferRequest request);

}

