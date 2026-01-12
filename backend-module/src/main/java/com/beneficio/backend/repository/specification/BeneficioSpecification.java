package com.beneficio.backend.repository.specification;

import com.beneficio.backend.dto.BeneficioFilter;
import com.beneficio.domain.entity.Beneficio;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class BeneficioSpecification {
    public static Specification<Beneficio> filterBy(BeneficioFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("nome")),
                        "%" + filtro.nome().toLowerCase() + "%"
                ));
            }

            if (filtro.ativo() != null) {
                predicates.add(cb.equal(root.get("ativo"), filtro.ativo()));
            }

            if (filtro.valorMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("valor"), filtro.valorMin()));
            }

            if (filtro.valorMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("valor"), filtro.valorMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
