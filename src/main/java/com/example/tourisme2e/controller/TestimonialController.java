package com.example.tourisme2e.controller;

import com.example.tourisme2e.entity.StatutModeration;
import com.example.tourisme2e.entity.Testimonial;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialRepository repository;

    @GetMapping
    public List<Testimonial> lister() {
        return repository.findAll();
    }

    @GetMapping("/accueil")
    public List<Testimonial> accueil() {
        return repository.findByStatutAndAfficherAccueil(StatutModeration.APPROUVE, true);
    }

    @PostMapping
    public Testimonial creer(@RequestBody Testimonial request) {
        request.setId(null);
        return repository.save(request);
    }

    @PutMapping("/{id}")
    public Testimonial modifier(@PathVariable Long id, @RequestBody Testimonial request) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Avis non trouve : " + id);
        }
        request.setId(id);
        return repository.save(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Avis non trouve : " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
