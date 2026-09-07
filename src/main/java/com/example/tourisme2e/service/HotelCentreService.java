package com.example.tourisme2e.service;

import com.example.tourisme2e.entity.HotelCentre;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.HotelCentreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelCentreService {

    private final HotelCentreRepository hotelCentreRepository;

    @Transactional(readOnly = true)
    public List<HotelCentre> lister() {
        return hotelCentreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public HotelCentre getOne(Long id) {
        return hotelCentreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel/Centre non trouve : " + id));
    }

    @Transactional
    public HotelCentre creer(HotelCentre request) {
        request.setId(null);
        return hotelCentreRepository.save(request);
    }

    @Transactional
    public HotelCentre modifier(Long id, HotelCentre request) {
        if (!hotelCentreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel/Centre non trouve : " + id);
        }
        request.setId(id);
        return hotelCentreRepository.save(request);
    }

    @Transactional
    public void supprimer(Long id) {
        if (!hotelCentreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel/Centre non trouve : " + id);
        }
        hotelCentreRepository.deleteById(id);
    }
}
