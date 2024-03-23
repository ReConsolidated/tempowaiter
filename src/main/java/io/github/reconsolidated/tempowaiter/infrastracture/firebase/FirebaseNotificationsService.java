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
        Message msg = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setBody("Nowe zgłoszenie przy stoliku " + request.getTableName())
                        .setTitle("Stolik " + request.getTableName())
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setIcon("ic_launcher")
                                .setBody("Nowe zgłoszenie przy stoliku " + request.getTableName())
                                .setTitle("Stolik " + request.getTableName())
                                .setColor("#f45342")
                                .setChannelId("waiter_requests")
                                .setTag("waiter_requests")
                                .setSound("custom_sound")
                                .build())
                        .build())
                .putData("body", "Nowe zgłoszenie przy stoliku " + request.getTableName())
                .build();
        try {
            firebaseMessaging.send(msg);
            System.out.println("sent notification");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
