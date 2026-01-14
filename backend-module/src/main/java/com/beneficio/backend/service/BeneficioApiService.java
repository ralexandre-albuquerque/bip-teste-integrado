package com.beneficio.backend.service;

import com.beneficio.backend.dto.TransferRequest;
import com.beneficio.domain.exception.BusinessException;
import com.beneficio.ejb.BeneficioEjbService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BeneficioApiService {

    private final BeneficioEjbService beneficioEjbService;

    public BeneficioApiService(BeneficioEjbService beneficioEjbService) {
        this.beneficioEjbService = beneficioEjbService;
    }
    /**
     * Orquestra a transferência chamando o serviço de negócio (EJB).
     */
    public void executeTransfer(TransferRequest request) {
        // Validação básica de valor antes de processar no EJB
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor da transferência deve ser maior que zero.");
        }

        // Delega a responsabilidade para o EJB que contém a lógica transacional
        beneficioEjbService.transfer(request.fromId(), request.toId(), request.amount());
    }


}
