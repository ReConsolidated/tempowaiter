package io.github.reconsolidated.tempowaiter.infrastracture.firebase;

import com.google.firebase.messaging.*;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirebaseNotificationsService {
    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(String topic, WaiterRequest request) {
        System.out.println("sending notification");
        Message msg = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setBody("Nowe zgłoszenie przy stoliku " + request.getTableName() + " - " + request.getType())
                        .setTitle("Stolik " + request.getTableName())
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setIcon("stock_ticker_update")
                                .setColor("#f45342")
                                .build())
                        .build())
                .putData("body", "Nowe zgłoszenie przy stoliku " + request.getTableName() + " - " + request.getType())
                .build();
        try {
            firebaseMessaging.send(msg);
            System.out.println("sent notification");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
