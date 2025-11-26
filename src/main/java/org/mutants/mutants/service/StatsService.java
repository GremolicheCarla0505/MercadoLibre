package org.mutants.mutants.service;

import lombok.RequiredArgsConstructor;
import org.mutants.mutants.dto.StatsResponse;
import org.mutants.mutants.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats() {
        long mutants = dnaRecordRepository.countByIsMutant(true);
        long humans = dnaRecordRepository.countByIsMutant(false);

        double ratio = humans == 0 ? mutants : (double) mutants / humans;

        return new StatsResponse(mutants, humans, ratio);
    }

}
