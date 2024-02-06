package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.infrastracture.firebase.FirebaseNotificationsService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class Notifier {
    private SimpMessagingTemplate simpMessagingTemplate;
    private FirebaseNotificationsService firebaseNotificationsService;

    public void sendRequestsNotification(Long companyId, WaiterRequest request, boolean shouldNotify) {
        simpMessagingTemplate.convertAndSend("/public/waiter_requests/" + companyId, request.getState().name());
        if (shouldNotify) {
            firebaseNotificationsService.sendNotification("waiter_requests_" + companyId, request);
        }
    }

    public void sendTableNotification(Long tableId, String message) {
        simpMessagingTemplate.convertAndSend("/public/table_updates/" + tableId, message);
    }
}
