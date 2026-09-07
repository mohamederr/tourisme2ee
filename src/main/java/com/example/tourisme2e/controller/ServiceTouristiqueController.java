package com.example.tourisme2e.controller;

import com.example.tourisme2e.entity.ServiceTouristique;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.ServiceTouristiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services-touristiques")
@RequiredArgsConstructor
public class ServiceTouristiqueController {

    private final ServiceTouristiqueRepository repository;

    @GetMapping
    public List<ServiceTouristique> lister() {
        return repository.findAll();
    }

    @PostMapping
    public ServiceTouristique creer(@RequestBody ServiceTouristique request) {
        request.setId(null);
        return repository.save(request);
    }

    @PutMapping("/{id}")
    public ServiceTouristique modifier(@PathVariable Long id, @RequestBody ServiceTouristique request) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Service non trouve : " + id);
        }
        request.setId(id);
        return repository.save(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Service non trouve : " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
