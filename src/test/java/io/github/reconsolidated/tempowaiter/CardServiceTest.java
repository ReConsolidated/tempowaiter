package io.github.reconsolidated.tempowaiter;

import io.github.reconsolidated.tempowaiter.card.Card;
import io.github.reconsolidated.tempowaiter.card.CardRepository;
import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
public class CardServiceTest {
    @Autowired
    private CardService cardService;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CompanyService companyService;

    @Test
    public void testSetCompanyIdAutomaticDisplayName() {
        Long companyId = 1L;

        Card card1 = createCard("111");
        Card card2 = createCard("112");
        Card card3 = createCard("113");

        cardService.setCardCompanyId(card1.getId(), companyId);
        cardService.setCardCompanyId(card2.getId(), companyId);
        cardService.setCardDisplayName(card1.getId(), "abc");
        cardService.setCardDisplayName(card2.getId(), "8");
        Card result = cardService.setCardCompanyId(card3.getId(), companyId);

        assertThat(result.getDisplayName()).isEqualTo("9");
    }

    private Card createCard(String cardUid) {
        Card card = new Card();
        card.setCardUid(cardUid);
        cardRepository.save(card);
        return card;
    }
}
