package com.beneficio.ejb;

import com.beneficio.domain.entity.Beneficio;
import com.beneficio.domain.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(rollbackFor = Exception.class)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateIdentifiers(fromId, toId);

        Beneficio from = getBeneficio(fromId, "from");
        Beneficio to = getBeneficio(toId, "to");

        from.withdraw(amount);
        to.deposit(amount);

        em.merge(from);
        em.merge(to);
    }

    private void validateIdentifiers(Long fromId, Long toId) {
        if (fromId == null || toId == null) {
            throw new BusinessException("IDs de origem e destino são obrigatórios.");
        }
        if (fromId.equals(toId)) {
            throw new BusinessException("A conta de origem e destino não podem ser iguais.");
        }
    }

    private Beneficio getBeneficio(Long id, String context) {
        Beneficio beneficio = em.find(Beneficio.class, id, LockModeType.OPTIMISTIC);
        if (beneficio == null) {
            throw new BusinessException(
                    String.format("Benefício de %s não encontrado (ID: %d)", context, id)
            );
        }
        return beneficio;
    }
}
