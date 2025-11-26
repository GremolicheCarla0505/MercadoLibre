package org.mutants.mutants.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.mutants.mutants.dto.StatsResponse;
import org.mutants.mutants.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    @Operation(
            summary = "Estadísticas de análisis",
            description = "Devuelve cantidad de ADN mutante, humano y ratio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas generadas correctamente")
    })
    @GetMapping
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }

}
