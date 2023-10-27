package io.github.reconsolidated.tempowaiter.card;

import io.github.reconsolidated.tempowaiter.ntag_decryption.NtagDecryptionService;
import io.github.reconsolidated.tempowaiter.ntag_decryption.NtagInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final NtagDecryptionService ntagDecryptionService;

    public long getCardCompanyId(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        return card.getCompanyId();
    }

    public Card setCardCompanyId(Long cardId, Long companyId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
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
}
