package com.root.calculadoraedenred.service;

import com.root.calculadoraedenred.dto.ScoreDTO;
import com.root.calculadoraedenred.dto.TransactionDTO;
import com.root.calculadoraedenred.model.Transaction;
import com.root.calculadoraedenred.model.enums.PaymentType;
import com.root.calculadoraedenred.repository.EmissionFactorRepository;
import com.root.calculadoraedenred.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final EmissionFactorRepository emissionFactorRepository;

    private static final Map<PaymentType, Double> DEFAULT_FACTORS = new EnumMap<>(Map.of(
        PaymentType.PHYSICAL, 500.0,
        PaymentType.TED,      150.0,
        PaymentType.PIX,        5.0,
        PaymentType.NFC,        3.0,
        PaymentType.UNKNOWN,  500.0
    ));

    public List<TransactionDTO> getHistory(Long companyId, LocalDate startDate, LocalDate endDate) {
        Map<PaymentType, Double> factors = buildFactorMap();
        return transactionRepository
            .findByCompanyIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                companyId,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX))
            .stream()
            .map(t -> toDTO(t, factors))
            .collect(Collectors.toList());
    }

    public ScoreDTO calculateScore(Long companyId, LocalDate startDate, LocalDate endDate) {
        Map<PaymentType, Double> factors = buildFactorMap();
        double physicalFactor = factors.getOrDefault(PaymentType.PHYSICAL, 500.0);

        List<Transaction> transactions = transactionRepository
            .findByCompanyIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                companyId,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX));

        double actualCO2 = transactions.stream()
            .mapToDouble(t -> factors.getOrDefault(t.getPaymentType(), physicalFactor))
            .sum();

        double baselineCO2 = transactions.size() * physicalFactor;
        double co2Saved = baselineCO2 - actualCO2;
        double score = baselineCO2 > 0
            ? Math.max(0, Math.min(100, (co2Saved / baselineCO2) * 100))
            : 0;

        long digitalCount = transactions.stream()
            .filter(t -> t.getPaymentType() != PaymentType.PHYSICAL
                      && t.getPaymentType() != PaymentType.UNKNOWN)
            .count();

        return new ScoreDTO(score, actualCO2, baselineCO2, co2Saved,
            transactions.size(), digitalCount, startDate, endDate);
    }

    private Map<PaymentType, Double> buildFactorMap() {
        Map<PaymentType, Double> factors = new EnumMap<>(DEFAULT_FACTORS);
        emissionFactorRepository.findAll()
            .forEach(ef -> factors.put(ef.getPaymentType(), ef.getCo2GramsPerTransaction()));
        return factors;
    }

    private TransactionDTO toDTO(Transaction t, Map<PaymentType, Double> factors) {
        double co2 = factors.getOrDefault(t.getPaymentType(),
            DEFAULT_FACTORS.getOrDefault(t.getPaymentType(), 500.0));
        return new TransactionDTO(
            t.getId(), t.getCompanyId(), t.getPaymentType().name(),
            t.getAmount(), t.getTransactionDate(), co2);
    }
}
