package io.github.reconsolidated.tempowaiter.authentication.verification;

import io.github.reconsolidated.tempowaiter.infrastracture.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationService {
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;


    public void sendVerificationToken(String email) {
        Optional<VerificationToken> token = verificationTokenRepository.findByEmail(email);
        if (token.isPresent()) {
            if (token.get().getLastResent().isAfter(LocalDateTime.now().minusMinutes(10))) {
                throw new IllegalArgumentException("Token was already resent in the last 10 minutes.");
            }
            token.get().setLastResent(LocalDateTime.now());
            verificationTokenRepository.save(token.get());
        } else {
            token = Optional.of(VerificationToken.builder()
                    .email(email)
                    .token(UUID.randomUUID().toString())
                    .lastResent(LocalDateTime.now())
                    .build());
            verificationTokenRepository.save(token.get());
        }
        emailService.sendSimpleMessage(email, "TempoWaiter - weryfikacja konta",
                htmlMessage.replace("<token>", token.get().getToken()));
    }

    public String verify(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
        verificationTokenRepository.delete(verificationToken.get());
        return verificationToken.get().getEmail();
    }

    private final static String htmlMessage = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        color: #333;
                        line-height: 1.6;
                    }
                    .container {
                        max-width: 600px;
                        margin: 20px auto;
                        padding: 20px;
                        background: #fff;
                        border: 1px solid #ddd;
                    }
                    .button {
                        display: inline-block;
                        padding: 10px 20px;
                        margin-top: 20px;
                        background: #0056b3;
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                    }
                    .footer {
                        text-align: center;
                        font-size: 0.8em;
                        color: #666;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Witaj w TempoWaiter!</h2>
                    <p>Cześć!</p>
                    <p>Dziękujemy za rejestrację w naszej aplikacji. Jesteśmy podekscytowani, że dołączyłeś(-aś) do naszej społeczności.</p>
                   
                    <p>Aby rozpocząć, kliknij poniższy przycisk, aby aktywować swoje konto:</p>
                    <a href="https://tempowaiter.pl/aktywacja?token=<token>" class="button">Aktywuj konto</a>
                        
                    <p>Jeśli masz jakiekolwiek pytania, skontaktuj się z nami pod adresem <a href="mailto:kontakt@tempowaiter.pl">kontakt@tempowaiter.pl</a>.</p>
                        
                    <p>Pozdrawiamy,</p>
                    <p>Zespół TempoWaiter</p>
                </div>
                <div class="footer">
                    &copy; 2024 TempoWaiter. Wszelkie prawa zastrzeżone.
                </div>
            </body>
            </html>
                        
                        
            """;
}
