package com.example.tourisme2e.controller;

import com.example.tourisme2e.entity.Partenaire;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partenaires")
@RequiredArgsConstructor
public class PartenaireController {

    private final PartenaireRepository repository;

    @GetMapping
    public List<Partenaire> lister() {
        return repository.findAll();
    }

    @PostMapping
    public Partenaire creer(@RequestBody Partenaire request) {
        request.setId(null);
        return repository.save(request);
    }

    @PutMapping("/{id}")
    public Partenaire modifier(@PathVariable Long id, @RequestBody Partenaire request) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Partenaire non trouve : " + id);
        }
        request.setId(id);
        return repository.save(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Partenaire non trouve : " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
