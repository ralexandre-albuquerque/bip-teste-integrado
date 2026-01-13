package com.beneficio.backend.service;

import com.beneficio.backend.dto.BeneficioFilter;
import com.beneficio.backend.dto.BeneficioRequest;
import com.beneficio.backend.dto.BeneficioResponse;
import com.beneficio.backend.exception.BusinessException;
import com.beneficio.backend.mapper.BeneficioMapper;
import com.beneficio.backend.repository.BeneficioRepository;
import com.beneficio.backend.service.impl.BeneficioServiceImpl;
import com.beneficio.domain.entity.Beneficio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceImplTest {

    @Mock
    private BeneficioRepository repository;

    @Mock
    private BeneficioMapper mapper;

    @InjectMocks
    private BeneficioServiceImpl service;

    private Beneficio beneficioEntity;
    private BeneficioResponse beneficioResponse;

    @BeforeEach
    void setUp() {
        beneficioEntity = new Beneficio();
        beneficioEntity.setId(1L);
        beneficioEntity.setNome("Vale Alimentação");
        beneficioEntity.setDescricao("Descrição do benefício");
        beneficioEntity.setAtivo(true);
        beneficioEntity.setValor(new BigDecimal("500.00"));

        beneficioResponse = new BeneficioResponse(
                1L,
                "Vale Alimentação",
                "Descrição do benefício",
                new BigDecimal("500.00"),
                true
        );
    }

    @Nested
    @DisplayName("Método findAll")
    class FindAllMethod {

        @Test
        @DisplayName("Deve buscar benefícios com filtros e paginação")
        void shouldFindBeneficiosWithFiltersAndPagination() {
            // Arrange
            var filtro = new BeneficioFilter("alimentação", true, null, null);
            var pageable = PageRequest.of(0, 10);
            var pageEntity = new PageImpl<>(List.of(beneficioEntity), pageable, 1);

            when(repository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(pageEntity);
            when(mapper.toResponse(beneficioEntity))
                    .thenReturn(beneficioResponse);

            // Act
            var result = service.findAll(filtro, pageable);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(beneficioResponse);

            verify(repository).findAll(any(Specification.class), eq(pageable));
            verify(mapper).toResponse(beneficioEntity);
        }

        @Test
        @DisplayName("Deve retornar página vazia quando não encontrar resultados")
        void shouldReturnEmptyPageWhenNoResultsFound() {
            // Arrange
            var filtro = new BeneficioFilter("inexistente", null, null, null);
            var pageable = PageRequest.of(0, 10);
            var emptyPage = Page.<Beneficio>empty(pageable);

            when(repository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // Act
            var result = service.findAll(filtro, pageable);

            // Assert
            assertThat(result.getContent()).isEmpty();
            verify(repository).findAll(any(Specification.class), eq(pageable));
        }

        @Test
        @DisplayName("Deve chamar o repository com a specification correta")
        void shouldCallRepositoryWithCorrectSpecification() {
            // Arrange
            var filtro = new BeneficioFilter("teste", true, null, null);
            var pageable = PageRequest.of(0, 10);

            when(repository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(Page.empty());

            // Act
            service.findAll(filtro, pageable);

            // Assert
            verify(repository).findAll(any(Specification.class), eq(pageable));
        }
    }

    @Test
    @DisplayName("Deve criar um benefício com sucesso quando o nome não existe")
    void create_DeveCriarBeneficio_QuandoNomeNaoExiste() {
        // Arrange
        var request = new BeneficioRequest("Vale Refeição", "VR Diário", new BigDecimal("22.00"), true);
        var beneficioEntity = new Beneficio(); // Supondo que sua entidade tenha esses campos
        var beneficioResponse = new BeneficioResponse(1L, "Vale Refeição", "VR Diário", new BigDecimal("22.00"), true);

        when(repository.existsByNomeIgnoreCase(request.nome())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(beneficioEntity);
        when(repository.save(beneficioEntity)).thenReturn(beneficioEntity);
        when(mapper.toResponse(beneficioEntity)).thenReturn(beneficioResponse);

        // Act
        BeneficioResponse result = service.create(request);

        // Assert
        assertNotNull(result);
        assertEquals("Vale Refeição", result.nome());
        verify(repository).existsByNomeIgnoreCase(request.nome());
        verify(repository).save(beneficioEntity);
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar criar benefício com nome duplicado")
    void create_DeveLancarExcecao_QuandoNomeJaExiste() {
        // Arrange
        var request = new BeneficioRequest("Vale Refeição", "VR Diário", new BigDecimal("22.00"), true);

        when(repository.existsByNomeIgnoreCase(request.nome())).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.create(request);
        });

        assertEquals("Já existe um benefício cadastrado com o nome: Vale Refeição", exception.getMessage());
        verify(repository).existsByNomeIgnoreCase(request.nome());
        verify(repository, never()).save(any()); // Garante que não tentou salvar no banco
    }
}