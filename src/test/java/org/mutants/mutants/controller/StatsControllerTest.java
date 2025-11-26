package org.mutants.mutants.controller;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mutants.mutants.dto.StatsResponse;
import org.mutants.mutants.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
public class StatsControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatsService statsService;

    @Test
    void testStatsReturns200() throws Exception {
        Mockito.when(statsService.getStats())
                .thenReturn(new StatsResponse(2, 2, 1.0));

        mvc.perform(get("/stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countMutantDna").value(2))
                .andExpect(jsonPath("$.countHumanDna").value(2))
                .andExpect(jsonPath("$.ratio").value(1.0));
    }
}
