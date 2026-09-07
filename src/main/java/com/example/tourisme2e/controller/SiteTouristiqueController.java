package com.example.tourisme2e.controller;

import com.example.tourisme2e.entity.SiteTouristique;
import com.example.tourisme2e.service.SiteTouristiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites-touristiques")
@RequiredArgsConstructor
public class SiteTouristiqueController {

    private final SiteTouristiqueService siteTouristiqueService;

    @GetMapping
    public List<SiteTouristique> lister() {
        return siteTouristiqueService.lister();
    }

    @GetMapping("/{id}")
    public SiteTouristique getOne(@PathVariable Long id) {
        return siteTouristiqueService.getOne(id);
    }

    @PostMapping
    public SiteTouristique creer(@RequestBody SiteTouristique request) {
        return siteTouristiqueService.creer(request);
    }

    @PutMapping("/{id}")
    public SiteTouristique modifier(@PathVariable Long id, @RequestBody SiteTouristique request) {
        return siteTouristiqueService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        siteTouristiqueService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
