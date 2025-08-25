package com.app.recruitmentapp.repositories;
import com.app.recruitmentapp.entities.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findByUserId(Long userId);
    boolean existsByUserIdAndOfferId(Long userId, Long offerId);
}

