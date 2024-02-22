package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.infrastracture.firebase.FirebaseNotificationsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class Notifier {
    private SimpMessagingTemplate simpMessagingTemplate;
    private FirebaseNotificationsService firebaseNotificationsService;
    private final Logger logger = Logger.getLogger(Notifier.class.getName());

    public void sendRequestsNotification(Long companyId, WaiterRequest request, boolean shouldNotify) {
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
