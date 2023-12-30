package io.github.reconsolidated.tempowaiter.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TableSessionDto {
    private long tableId;
    private long cardId;
    private long ctr;
    private String sessionId;
    private Long startedAt;
    private Long lastRequestAt;
    private boolean isOverwritten = false;
    private LocalDateTime expirationDate;
    private Long companyId;
    private String companyName;
    private String menuLink;
    private List<String> backgroundImages;
    private String facebookLink;
    private String instagramLink;
    private String tiktokLink;
    private String googleReviewLink;

    public TableSessionDto(TableSession tableSession) {
        this.tableId = tableSession.getTableId();
        this.cardId = tableSession.getCardId();
        this.ctr = tableSession.getCtr();
        this.sessionId = tableSession.getSessionId();
        this.startedAt = tableSession.getStartedAt();
        this.lastRequestAt = tableSession.getLastRequestAt();
        this.isOverwritten = tableSession.isOverwritten();
        this.expirationDate = tableSession.getExpirationDate();
        this.companyId = tableSession.getCompany().getId();
        this.companyName = tableSession.getCompany().getName();
        this.menuLink = tableSession.getCompany().getMenuLink();
        this.backgroundImages = tableSession.getCompany().getBackgroundImages();
        this.facebookLink = tableSession.getCompany().getFacebookLink();
        this.instagramLink = tableSession.getCompany().getInstagramLink();
        this.tiktokLink = tableSession.getCompany().getTiktokLink();
        this.googleReviewLink = tableSession.getCompany().getGoogleReviewLink();
    }
}
