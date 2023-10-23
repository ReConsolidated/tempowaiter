package io.github.reconsolidated.tempowaiter.card;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;

    public long getCardCompanyId(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        return card.getCompanyId();
    }

    public Card setCardCompanyId(Long cardId, Long companyId) {
        Card card = cardRepository.findById(cardId).orElse(new Card(cardId, companyId));
        card.setCompanyId(companyId);
        cardRepository.save(card);
        return card;
    }

    public void setCardTableId(Long cardId, Long tableId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setTableId(tableId);
        cardRepository.save(card);
    }

    public List<Card> getCards(Long companyId) {
        return cardRepository.findAllByCompanyId(companyId);
    }

    public List<Card> getCards() {
        return cardRepository.findAll();
    }

    public void removeCardTableId(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setTableId(null);
        cardRepository.save(card);
    }
}
