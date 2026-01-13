package com.beneficio.ejb;

import com.beneficio.domain.entity.Beneficio;
import com.beneficio.domain.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do BeneficioEjbService")
class BeneficioEjbServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbService service;

    @Test
    @DisplayName("Deve lançar exceção quando os IDs de origem e destino forem iguais")
    void transferSameId() {
        var amount = new BigDecimal("50.00");

        var exception = assertThrows(BusinessException.class, () ->
                service.transfer(1L, 1L, amount)
        );

        assertTrue(exception.getMessage().contains("não podem ser iguais"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID de origem for nulo")
    void transferNullFromId() {
        var exception = assertThrows(BusinessException.class, () -> {
            service.transfer(null, 2L, BigDecimal.TEN);
        });
        assertTrue(exception.getMessage().contains("IDs de origem e destino são obrigatórios"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o benefício de origem não for encontrado")
    void transferOriginNotFound() {
        when(em.find(eq(Beneficio.class), anyLong(), eq(LockModeType.OPTIMISTIC))).thenReturn(null);

        var exception = assertThrows(BusinessException.class, () -> {
            service.transfer(1L, 2L, BigDecimal.TEN);
        });

        System.out.println("Mensagem da exceção: " + exception.getMessage());

        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o saldo for insuficiente")
    void transferInsufficientBalance() {
        var origin = createBeneficio(1L, BigDecimal.ONE);
        var destination = createBeneficio(2L, BigDecimal.ZERO);

        when(em.find(eq(Beneficio.class), eq(1L), eq(LockModeType.OPTIMISTIC))).thenReturn(origin);
        when(em.find(eq(Beneficio.class), eq(2L), eq(LockModeType.OPTIMISTIC))).thenReturn(destination);

        var exception = assertThrows(BusinessException.class, () -> {
            service.transfer(1L, 2L, BigDecimal.TEN);
        });
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
    }

    @Test
    @DisplayName("Deve realizar a transferência com sucesso entre duas contas válidas")
    void successfulTransfer() {
        var origin = createBeneficio(1L, new BigDecimal("100.00"));
        var destination = createBeneficio(2L, new BigDecimal("50.00"));
        var transferAmount = new BigDecimal("30.00");

        when(em.find(eq(Beneficio.class), eq(1L), eq(LockModeType.OPTIMISTIC))).thenReturn(origin);
        when(em.find(eq(Beneficio.class), eq(2L), eq(LockModeType.OPTIMISTIC))).thenReturn(destination);

        assertDoesNotThrow(() -> service.transfer(1L, 2L, transferAmount));

        assertAll("Verificar saldos após a transferência",
                () -> assertEquals(new BigDecimal("70.00"), origin.getValor(), "Origem deve ser debitada"),
                () -> assertEquals(new BigDecimal("80.00"), destination.getValor(), "Destino deve ser creditado")
        );

        verify(em, times(2)).merge(any(Beneficio.class));
    }

    private Beneficio createBeneficio(Long id, BigDecimal balance) {
        var b = new Beneficio();
        b.setId(id);
        b.setValor(balance);
        return b;
    }

}