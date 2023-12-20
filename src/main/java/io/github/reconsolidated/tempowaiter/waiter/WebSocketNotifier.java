package io.github.reconsolidated.tempowaiter.waiter;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class WebSocketNotifier {
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendRequestsNotification(Long companyId, String message) {
        simpMessagingTemplate.convertAndSend("/public/waiter_requests/" + companyId, message);

    }

    public void sendTableNotification(Long tableId, String message) {
        simpMessagingTemplate.convertAndSend("/public/table_updates/" + tableId, message);
    }
}
