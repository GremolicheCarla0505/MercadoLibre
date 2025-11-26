package org.mutants.mutants.detector;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {

        // -----------------------------
        // VALIDACIONES
        // -----------------------------
        if (dna == null || dna.length == 0) return false;
        if (dna.length < 4) return false;

        int n = dna.length;

        // Validar NxN + caracteres válidos
        for (String row : dna) {
            if (row == null) return false;
            if (row.length() != n) return false;

            for (char c : row.toCharArray()) {
                if (!isValidBase(c)) return false;
            }
        }

        // Convertir a matriz char[][] (eficiencia)
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // -----------------------------
        // DETECCIÓN DE SECUENCIAS
        // -----------------------------
        int sequenceCount = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Horizontal →
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true; // early termination
                    }
                }

                // Vertical ↓
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // Diagonal ↘
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // Diagonal ↗
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }

        // Si llegamos acá: NO mutante (0 o 1 secuencia)
        return false;
    }

    // -----------------------------
    // MÉTODOS PRIVADOS
    // -----------------------------

    private boolean isValidBase(char c) {
        return c == 'A' || c == 'T' || c == 'C' || c == 'G';
    }

    private boolean checkHorizontal(char[][] m, int row, int col) {
        char base = m[row][col];
        return m[row][col + 1] == base &&
                m[row][col + 2] == base &&
                m[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] m, int row, int col) {
        char base = m[row][col];
        return m[row + 1][col] == base &&
                m[row + 2][col] == base &&
                m[row + 3][col] == base;
    }

    private boolean checkDiagonalDescending(char[][] m, int row, int col) {
        char base = m[row][col];
        return m[row + 1][col + 1] == base &&
                m[row + 2][col + 2] == base &&
                m[row + 3][col + 3] == base;
    }

    private boolean checkDiagonalAscending(char[][] m, int row, int col) {
        char base = m[row][col];
        return m[row - 1][col + 1] == base &&
                m[row - 2][col + 2] == base &&
                m[row - 3][col + 3] == base;
    }
}
