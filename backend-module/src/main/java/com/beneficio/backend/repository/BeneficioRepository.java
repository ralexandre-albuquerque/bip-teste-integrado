package com.beneficio.backend.repository;

import com.beneficio.domain.entity.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.*;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long>, JpaSpecificationExecutor<Beneficio> {

}
