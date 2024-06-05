package io.github.reconsolidated.tempowaiter.domain.authentication.appUser;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppUserDto {
    private String email;
    private Long companyId;
    private Boolean isVerified;
}
