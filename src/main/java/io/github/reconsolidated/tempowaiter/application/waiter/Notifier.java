package io.github.reconsolidated.tempowaiter.application.waiter;

import io.github.reconsolidated.tempowaiter.domain.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.infrastracture.firebase.FirebaseNotificationsService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
@AllArgsConstructor
public class Notifier {
    private SimpMessagingTemplate simpMessagingTemplate;
    private FirebaseNotificationsService firebaseNotificationsService;

    public void sendRequestsNotification(Long companyId, WaiterRequest request, boolean shouldNotify) {
        Logger logger = Logger.getLogger(Notifier.class.getName());
        logger.info("Sending websocket notification for company id: " + companyId);
        simpMessagingTemplate.convertAndSend("/public/waiter_requests/" + companyId, request.getState().name());
        if (shouldNotify) {
            logger.info("Sending firebase notification for company id: " + companyId);
            firebaseNotificationsService.sendNotification("waiter_requests_" + companyId, request);
        }
    }

    public void sendTableNotification(Long tableId, String message) {
        simpMessagingTemplate.convertAndSend("/public/table_updates/" + tableId, message);
    }
}
