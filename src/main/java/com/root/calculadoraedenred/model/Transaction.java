package com.root.calculadoraedenred.model;

import com.root.calculadoraedenred.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId; // empresa dona da transação

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private Double amount; // valor da transação

    @Column(nullable = false)
    private LocalDateTime transactionDate;
}