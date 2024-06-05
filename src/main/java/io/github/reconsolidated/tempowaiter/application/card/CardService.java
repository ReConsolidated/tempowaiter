package io.github.reconsolidated.tempowaiter.application.card;

import io.github.reconsolidated.tempowaiter.domain.card.Card;
import io.github.reconsolidated.tempowaiter.domain.card.CardRepository;
import io.github.reconsolidated.tempowaiter.infrastracture.ntagDecryption.NtagDecryptionService;
import io.github.reconsolidated.tempowaiter.infrastracture.ntagDecryption.NtagInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final NtagDecryptionService ntagDecryptionService;

    public Long getCardCompanyId(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        return card.getCompanyId();
    }

    public Card setCardDisplayName(Long cardId, String displayName) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setDisplayName(displayName);
        cardRepository.save(card);
        return card;
    }

    public Card setCardCompanyId(Long cardId, Long companyId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setCompanyId(companyId);
        card.setTableId(null);

        if (card.getDisplayName() == null) {
            card.setDisplayName(getLowestAvailableDisplayName(companyId));
        }
        cardRepository.save(card);
        return card;
    }

    private String getLowestAvailableDisplayName(Long companyId) {
        List<Card> cards = cardRepository.findAllByCompanyId(companyId);
        int highestCardId = cards.stream().mapToInt(card -> {
            try {
                return Integer.parseInt(card.getDisplayName());
            } catch (NumberFormatException e) {
                return 0;
            }
        }).max().orElse(0);
        return String.valueOf(highestCardId + 1);
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

    public Long getCardId(String cardUid) {
        return cardRepository.findByCardUid(cardUid).orElseThrow().getId();
    }

    public Card createCard(String e) {
        NtagInfo ntagInfo = ntagDecryptionService.decryptNtag(e);
        var potentialCard = cardRepository.findByCardUid(ntagInfo.getCardId());
        if (potentialCard.isPresent()) {
            throw new IllegalArgumentException("Card already exists. Id is %d".formatted(potentialCard.get().getId()));
        }
        Card card = new Card(ntagInfo.getCardId());

        return cardRepository.save(card);
    }

    public boolean deleteCard(Long cardId) {
        var card = cardRepository.findById(cardId);
        if (card.isEmpty()) return false;
        cardRepository.deleteById(cardId);
        return true;
    }

    public Optional<Card> getCardByTable(Long tableId) {
        return cardRepository.findByTableId(tableId);
    }
}
