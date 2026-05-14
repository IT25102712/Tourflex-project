package com.tourflex.service;

import com.tourflex.model.SavedCard;
import com.tourflex.repository.SavedCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedCardService {

    @Autowired
    private SavedCardRepository savedCardRepository;

    public SavedCard saveCard(SavedCard savedCard) {
        return savedCardRepository.save(savedCard);
    }

    public List<SavedCard> getCardsByEmail(String email) {
        return savedCardRepository.findByCustomerEmail(email);
    }

    public void deleteCard(int id) {
        savedCardRepository.deleteById(id);
    }

    public SavedCard getCardById(int id) {
        return savedCardRepository.findById(id).orElse(null);
    }
}