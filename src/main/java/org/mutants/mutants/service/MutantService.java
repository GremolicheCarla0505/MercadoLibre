package org.mutants.mutants.service;

import lombok.RequiredArgsConstructor;
import org.mutants.mutants.detector.MutantDetector;
import org.mutants.mutants.entity.DnaRecord;
import org.mutants.mutants.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.mutants.mutants.exception.InvalidDnaException;

import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    public boolean isMutant(String[] dna){
        validateDna(dna);
        String hash = calculateHash(dna);

        // 2. Buscar en BD (CACHE)
        return dnaRecordRepository.findByDnaHash(hash)
                .map(DnaRecord::isMutant)
                .orElseGet(() -> {
                    // 3. Analizar por primera vez
                    boolean isMutant = mutantDetector.isMutant(dna);

                    // 4. Guardar en BD
                    DnaRecord record = DnaRecord.builder()
                            .dnaHash(hash)
                            .isMutant(isMutant)
                            .createdAt(LocalDateTime.now())
                            .build();

                    dnaRecordRepository.save(record);

                    return isMutant;
                });
    }
    // Convertir ADN → hash SHA-256
    private String calculateHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String joined = String.join("", dna);
            byte[] hash = digest.digest(joined.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
    // Validar que el ADN tenga formato correcto
    private void validateDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            throw new InvalidDnaException("DNA no puede ser null o vacío");
        }

        int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new InvalidDnaException("La matriz de ADN debe ser NxN");
            }
            if (!row.matches("[ATCG]+")) {
                throw new InvalidDnaException("El ADN contiene caracteres inválidos");
            }
        }
    }

}
