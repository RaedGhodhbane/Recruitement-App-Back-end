package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer,Long> {
}
