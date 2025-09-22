package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    void deleteByExpirationDateBefore(LocalDate date);
}
