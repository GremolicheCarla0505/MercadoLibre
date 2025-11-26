package org.mutants.mutants.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.mutants.mutants.dto.DnaRequest;
import org.mutants.mutants.service.MutantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mutant")
@RequiredArgsConstructor
public class MutantController {

private final MutantService mutantService;

@PostMapping
@Operation(
        summary = "Verifica si un ADN pertenece a un mutante",
        description = "Devuelve 200 si es mutante, 403 si es humano."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mutante detectado"),
        @ApiResponse(responseCode = "403", description = "No es mutante"),
        @ApiResponse(responseCode = "400", description = "ADN inválido")
})

    public ResponseEntity<Void> checkMutant(@RequestBody DnaRequest request){
    boolean isMutant = mutantService.isMutant(request.getDna());

        if(isMutant){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                     .build();
        }
    }
}
