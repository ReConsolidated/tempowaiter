package io.github.reconsolidated.tempowaiter.waiter;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class WebSocketNotifier {
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendRequestsNotification(Long companyId) {
        simpMessagingTemplate.convertAndSend("/public/waiter_requests/" + companyId, "New data available");

    }

    public void sendTableNotification(Long tableId) {
        simpMessagingTemplate.convertAndSend("/public/table_updates/" + tableId, "New data available");
    }
}
