package org.mutants.mutants.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mutants.mutants.detector.MutantDetector;
import org.mutants.mutants.entity.DnaRecord;
import org.mutants.mutants.repository.DnaRecordRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    private final String[] MUTANT_DNA = {
            "ATGCGA","CAGTGC","TTATGT",
            "AGAAGG","CCCCTA","TCACTG"
    };

    private final String[] HUMAN_DNA = {
            "ATGCGA","CAGTGC","TTATGT",
            "AGACGG","GCGTCA","TCACTG"
    };

    @Test
    void whenNewMutantDna_thenSavesAndReturnsTrue() {
        // no existe en BD
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        // el detector dice que es mutante
        when(mutantDetector.isMutant(MUTANT_DNA)).thenReturn(true);

        boolean result = mutantService.isMutant(MUTANT_DNA);

        assertTrue(result);
        verify(mutantDetector, times(1)).isMutant(MUTANT_DNA);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    void whenNewHumanDna_thenSavesAndReturnsFalse() {
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(HUMAN_DNA)).thenReturn(false);

        boolean result = mutantService.isMutant(HUMAN_DNA);

        assertFalse(result);
        verify(mutantDetector, times(1)).isMutant(HUMAN_DNA);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    void whenDnaAlreadyExists_thenUsesCachedResultAndDoesNotReAnalyze() {
        DnaRecord record = DnaRecord.builder()
                .id(1L)
                .dnaHash("hash-x")
                .isMutant(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.of(record));

        boolean result = mutantService.isMutant(MUTANT_DNA);

        assertTrue(result);
        // no recalcula nada
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }
}
