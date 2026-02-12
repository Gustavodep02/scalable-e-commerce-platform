package com.gustavo.notification_service.service;

import com.gustavo.notification_service.client.UserClient;
import com.gustavo.notification_service.model.JsonMessage;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserClient userClient;
    @Value("${sendgrid.api.key}")
    private String apikey;
    public void sendNotificationCancelled(JsonMessage message) {
        String emailTo = userClient.getUser(message.getUserId()).email();

        Email from = new Email("gdepaulay@hotmail.com");
        Email to = new Email(emailTo);
        String subject = "Order Notification";
        Content content = new Content("text/plain", "Your order has been cancelled.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apikey);
        Request request = new Request();
        try {
            request.setMethod(com.sendgrid.Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        }
        catch (Exception ex) {
            log.error("Error sending cancellation email: {}", ex.getMessage());
        }
    }
    public void sendNotificationPaid(JsonMessage message) {
        String emailTo = userClient.getUser(message.getUserId()).email();

        Email from = new Email("gdepaulay@hotmail.com");
        Email to = new Email(emailTo);
        String subject = "Order Notification";
        Content content = new Content("text/plain", "Your order has been paid.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apikey);
        Request request = new Request();
        try {
            request.setMethod(com.sendgrid.Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        }
        catch (Exception ex) {
            log.error("Error sending paid email: {}", ex.getMessage());
        }
    }
    public void sendNotificationCreated(JsonMessage message) {
        String emailTo = userClient.getUser(message.getUserId()).email();

        Email from = new Email("gdepaulay@hotmail.com");
        Email to = new Email(emailTo);
        String subject = "Order Notification";
        Content content = new Content("text/plain", "Your order has been created.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apikey);
        Request request = new Request();
        try {
            request.setMethod(com.sendgrid.Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        }
        catch (Exception ex) {
            log.error("Error sending created email: {}", ex.getMessage());
        }
    }
}
