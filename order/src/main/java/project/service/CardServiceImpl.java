package project.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Card;
import org.springframework.stereotype.Service;
import project.repository.CardRepository;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    @Override
    public Card buyCard(Card card) {
        cardRepository.save(card);
        return card;
    }
}
