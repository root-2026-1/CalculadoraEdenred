package com.root.calculadoraedenred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    private double score;
    private double actualCO2;
    private double baselineCO2;
    private double co2Saved;
    private long totalTransactions;
    private long digitalTransactions;
    private LocalDate startDate;
    private LocalDate endDate;
}
