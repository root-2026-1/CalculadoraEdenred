package com.root.calculadoraedenred.repository;

import com.root.calculadoraedenred.model.EmissionFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmissionFactorRepository
        extends JpaRepository<EmissionFactor, Long> {
}