package project.service;

import model.Card;
import org.springframework.stereotype.Service;
import project.repository.CardRepository;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    @Override
    public Card buyCard(Card card) {
        cardRepository.save(card);
        return card;
    }
}
