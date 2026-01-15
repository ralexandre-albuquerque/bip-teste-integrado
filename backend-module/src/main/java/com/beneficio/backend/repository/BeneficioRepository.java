package com.beneficio.backend.repository;

import com.beneficio.domain.entity.Beneficio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.*;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long>, JpaSpecificationExecutor<Beneficio> {

    boolean existsByNomeIgnoreCase(@NotBlank(message = "O nome é obrigatório") @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres") String nome);
}
