package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.PavillonRequest;
import com.example.tourisme2e.dto.PavillonResponse;
import com.example.tourisme2e.service.PavillonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pavillons")
@RequiredArgsConstructor
public class PavillonController {

    private final PavillonService pavillonService;

    @GetMapping
    public ResponseEntity<List<PavillonResponse>> lister() {
        return ResponseEntity.ok(pavillonService.lister());
    }

    @PostMapping
    public ResponseEntity<PavillonResponse> creer(@Valid @RequestBody PavillonRequest request) {
        return ResponseEntity.ok(pavillonService.creer(request));
    }
}
