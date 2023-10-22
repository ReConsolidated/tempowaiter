package io.github.reconsolidated.tempowaiter.waiter;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

@Controller
@AllArgsConstructor
public class WaiterRequestsBroker {
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/public/news")
    public void broadcastNews(@Payload String message) {
        this.simpMessagingTemplate.convertAndSend("/public/topic/news", message);
    }

    @MessageMapping("/public/greetings")
    @SendToUser("/public/queue/greetings")
    public String reply(@Payload String message,
                        Principal user) {
        return  "Hello " + message;
    }

    public void sendRequests(Long companyId, List<WaiterRequest> requests) {
        Logger.getLogger("abc").info("Sending requests");
        simpMessagingTemplate.convertAndSend("/public/waiter_requests/" + companyId, requests);
    }
}
