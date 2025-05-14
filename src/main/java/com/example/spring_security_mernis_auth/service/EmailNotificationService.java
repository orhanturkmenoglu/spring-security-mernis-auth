package com.example.spring_security_mernis_auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Service
public class EmailNotificationService {

    private final SnsClient snsClient;

    @Value("${AWS_TOPIC_ARN}")
    private String topicArn;

    public EmailNotificationService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void addEmailSubscriber(String email) {
        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol("email")
                .endpoint(email)
                .build();

        snsClient.subscribe(subscribeRequest);
    }

    public void sendLoginNotificationEmail(String email) {
        String message = "T.C. Kimlik numarası doğrulandı ve kullanıcı giriş yaptı.";
        String subject = "MERNIS Uygulaması Giriş";

        PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .subject(subject)
                .build();

        snsClient.publish(publishRequest);
    }
}
