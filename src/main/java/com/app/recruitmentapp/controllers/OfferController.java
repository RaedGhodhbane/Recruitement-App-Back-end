package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.services.OfferService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @GetMapping("/offers")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {
        Optional<Offer> offer = offerService.getOfferById(id);
        return offer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{idRecruiter}")
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer, @PathVariable Long idRecruiter) {
        Offer savedOffer = offerService.saveOffer(offer,idRecruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
        try {
            Offer updatedOffer = offerService.updateOffer(id, offer);
            return ResponseEntity.ok(updatedOffer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        try {
            offerService.deleteOffer(id);
            return ResponseEntity.ok("Offer deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
