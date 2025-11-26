package org.mutants.mutants.controller;

import org.junit.jupiter.api.Test;
import org.mutants.mutants.exception.InvalidDnaException;
import org.mutants.mutants.service.MutantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @Test
    void whenInvalidDna_thenReturn400() throws Exception {

        String json = """
                { "dna": ["AAA", "AA", "AAAA"] }
                """;

        doThrow(new InvalidDnaException("La matriz de ADN debe ser NxN"))
                .when(mutantService).isMutant(new String[]{"AAA","AA","AAAA"});

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
