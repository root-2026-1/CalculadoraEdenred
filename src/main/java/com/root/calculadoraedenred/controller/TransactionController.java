package com.root.calculadoraedenred.controller;

import com.root.calculadoraedenred.dto.ScoreDTO;
import com.root.calculadoraedenred.dto.TransactionDTO;
import com.root.calculadoraedenred.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/history")
    public ResponseEntity<List<TransactionDTO>> getHistory(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.getHistory(companyId, startDate, endDate));
    }

    @GetMapping("/score")
    public ResponseEntity<ScoreDTO> getScore(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.calculateScore(companyId, startDate, endDate));
    }
}
