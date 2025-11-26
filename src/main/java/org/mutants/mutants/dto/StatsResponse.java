package org.mutants.mutants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private long countMutantDna;
    private long countHumanDna;
    private double ratio;
}
