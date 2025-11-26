package org.mutants.mutants.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dnaHash;     // hash SHA-256

    @Column(nullable = false)
    private boolean isMutant;   // true = mutante, false = humano

    @Column(nullable = false)
    private LocalDateTime createdAt;



}
