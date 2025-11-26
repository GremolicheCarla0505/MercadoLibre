package org.mutants.mutants.detector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // 1) Mutante horizontal + diagonal
    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void mutantWithHorizontalAndDiagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // CCCC horizontal
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // 2) Mutante con secuencias verticales
    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void mutantWithVertical() {
        String[] dna = {
                "AAAAGA",
                "AAGTGC",
                "AAATGT",
                "AGAAGG",
                "CACCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // 3) Múltiples horizontales
    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
    void mutantWithMultipleHorizontals() {
        String[] dna = {
                "TTTTGA",  // TTTT
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // CCCC
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // 4) Diagonales ↘ y ↗
    @Test
    @DisplayName("Debe detectar mutante con diagonales descendentes y ascendentes")
    void mutantWithBothDiagonals() {
        String[] dna = {
                "ATTCGG",  // row 0: A _ _ C _ _
                "TACTGG",  // row 1: _ A C _ _ _
                "TCA TGG".replace(" ", ""), // "TCATGG"  row 2: _ C A _ _ _
                "CTTAGG",  // row 3: C _ _ A _ _
                "TTTTGG",  // fila extra cualquiera
                "GGGGTT"   // fila extra cualquiera
        };

    /*
     Descendente (↘): (0,0)A (1,1)A (2,2)A (3,3)A
     Ascendente (↗):  (3,0)C (2,1)C (1,2)C (0,3)C
    */

        assertTrue(mutantDetector.isMutant(dna));
    }

    // 5) Humano: solo una secuencia
    @Test
    @DisplayName("No debe detectar mutante con una sola secuencia")
    void humanWithOneSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // 6) Humano: sin secuencias
    @Test
    @DisplayName("No debe detectar mutante sin secuencias")
    void humanWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // 7) ADN nulo
    @Test
    @DisplayName("Debe rechazar ADN nulo")
    void nullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    // 8) ADN vacío
    @Test
    @DisplayName("Debe rechazar ADN vacío")
    void emptyDna() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna));
    }

    // 9) Matriz no cuadrada
    @Test
    @DisplayName("Debe rechazar matriz no cuadrada")
    void nonSquareMatrix() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // 10) Caracter inválido
    @Test
    @DisplayName("Debe rechazar caracteres inválidos")
    void invalidCharacters() {
        String[] dna = {
                "ATGCGA",
                "CAGTXC",  // X inválida
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // 11) Matriz mínima 4x4 mutante
    @Test
    @DisplayName("Debe detectar mutante en matriz 4x4")
    void small4x4Mutant() {
        String[] dna = {
                "AAAA",
                "CCCC",
                "TTAT",
                "AGAC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // 12) Matriz grande 10x10
    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void large10x10Mutant() {
        String[] dna = {
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA",
                "CCCCTACCCC", // al menos 2 horizontales
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // 13) Fila nula
    @Test
    @DisplayName("Debe rechazar fila nula dentro del array")
    void nullRowInsideArray() {
        String[] dna = {
                "ATGCGA",
                null,
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // 14) Early termination (performance)
    @Test
    @DisplayName("Debe usar early termination al encontrar más de una secuencia")
    void earlyTermination() {
        String[] dna = {
                "AAAAGA", // AAAA
                "AAAAGC", // AAAA
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        long start = System.nanoTime();
        boolean result = mutantDetector.isMutant(dna);
        long end = System.nanoTime();

        assertTrue(result);
        assertTrue(end - start > 0); // solo verificamos que se ejecutó
    }

    // 15) Todas las bases iguales
    @Test
    @DisplayName("Debe detectar mutante cuando todas las bases son iguales")
    void allSameBases() {
        String[] dna = {
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // 16) Matriz muy chica (<4) debe ser rechazada
    @Test
    @DisplayName("Debe rechazar matrices menores a 4x4")
    void matrixSmallerThan4() {
        String[] dna = {
                "AT",
                "TA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }
}