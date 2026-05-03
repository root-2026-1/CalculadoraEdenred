package com.root.calculadoraedenred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SustainabilityScoreResponse {

    private Long companyId;
    private Long totalTransactions;
    private Double co2RealGrams;
    private Double co2WorstCaseGrams;
    private Double co2SavedGrams;
    private Double sustainabilityScore;
    private String scoreLabel;
}
