package io.github.reconsolidated.tempowaiter.ntag_decryption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NtagInfo {
    private String dataTag;
    private String cardId;
    private Long ctr;
}
