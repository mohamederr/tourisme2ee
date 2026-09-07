package com.example.tourisme2e.controller;

import com.example.tourisme2e.entity.HotelCentre;
import com.example.tourisme2e.service.HotelCentreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels-centres")
@RequiredArgsConstructor
public class HotelCentreController {

    private final HotelCentreService hotelCentreService;

    @GetMapping
    public List<HotelCentre> lister() {
        return hotelCentreService.lister();
    }

    @GetMapping("/{id}")
    public HotelCentre getOne(@PathVariable Long id) {
        return hotelCentreService.getOne(id);
    }

    @PostMapping
    public HotelCentre creer(@RequestBody HotelCentre request) {
        return hotelCentreService.creer(request);
    }

    @PutMapping("/{id}")
    public HotelCentre modifier(@PathVariable Long id, @RequestBody HotelCentre request) {
        return hotelCentreService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        hotelCentreService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
