package io.github.reconsolidated.tempowaiter.api;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.payments.StripeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class StripeController {

    private final String endpointSecret;
    private final String endpointSecretTest = "whsec_NjEDDAjtOpypyOJJsrjQS8EY0YZP5KvE";
    private final Logger logger = Logger.getLogger(getClass().getName());

    public StripeController(@Value("${STRIPE_SECRET}") String stripeSecret,
                            @Value("${STRIPE_API_KEY}") String apiKey) {
        this.endpointSecret = stripeSecret;
        if (Objects.equals(this.endpointSecret, endpointSecretTest)) {
            logger.warning("stripe production secret is the same value as stripe test secret. " +
                    "If you are using production setup, make sure you set STRIPE_SECRET environment variable to " +
                    "stripe secret key (starting with whsec_...)");
        }
        Stripe.apiKey = apiKey;
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        Event event;
        try {
            try {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret
                );
            } catch (StripeException e) {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecretTest);
            }
        } catch (StripeException e) {
            logger.warning("Stripe event received, but it's incorrect: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook Error: " + e.getMessage());
        }
        logger.info("Stripe event received: " + event.getType());

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;
        }

        return ResponseEntity.ok("Event received");
    }

    private void handleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        Session session = (Session) dataObjectDeserializer.deserializeUnsafe();

        String email = session.getCustomerDetails().getEmail();
        Long amount = session.getAmountTotal();
        logger.info("Checkout Session completed for customer %s and amount %d".formatted(email, amount));
    }
}
