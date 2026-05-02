package com.root.calculadoraedenred.model;

import com.root.calculadoraedenred.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "emission_factors")
@Data  // Lombok
@NoArgsConstructor
@AllArgsConstructor
public class EmissionFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PaymentType paymentType;

    @Column(nullable = false)
    private Double co2GramsPerTransaction; // ex: PIX evita 15g, NFC evita 12g
}