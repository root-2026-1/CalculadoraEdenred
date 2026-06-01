package com.root.calculadoraedenred.repository;

import com.root.calculadoraedenred.model.SavedScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedScenarioRepository extends JpaRepository<SavedScenario, Long> {

    List<SavedScenario> findByEmpresaIdOrderByCriadoEmDesc(Long empresaId);
}
