package com.root.calculadoraedenred.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.root.calculadoraedenred.model.enums.CategoriaEstabelecimento;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_scenarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedScenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private Long empresaId;

    @Column(nullable = false)
    private Double emissoesAtuaisGramas;

    @Column(nullable = false)
    private Double emissoesSimuladasGramas;

    @Column(nullable = false)
    private Double economiaGramas;

    @Column(nullable = false)
    private Double percentualReducao;

    @Column(length = 255)
    private String descricao;

    @Column(length = 50)
    private String tipoMeio;

    @Enumerated(EnumType.STRING)
    @Column(length = 80)
    private CategoriaEstabelecimento categoria;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}
