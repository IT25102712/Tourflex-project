package com.tourflex.repository;

import com.tourflex.model.SavedCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedCardRepository extends JpaRepository<SavedCard, Integer> {
    List<SavedCard> findByCustomerEmail(String customerEmail);
}