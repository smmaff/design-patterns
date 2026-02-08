package homework3.DIP;

interface MessageSender {
    void send(String message);
}

class EmailSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("Email sent: " + message);
    }
}

class SmsSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("SMS sent: " + message);
    }
}

class MessengerSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("Messenger message sent: " + message);
    }
}

class NotificationService {
    private final MessageSender sender;

    public NotificationService(MessageSender sender) {
        this.sender = sender;
    }

    public void sendNotification(String message) {
        sender.send(message);
    }
}
public class DipDemo {
    public static void main(String[] args) {
        NotificationService emailService = new NotificationService(new EmailSender());
        emailService.sendNotification("Hello via email");

        NotificationService smsService = new NotificationService(new SmsSender());
        smsService.sendNotification("Hello via SMS");

        NotificationService messengerService = new NotificationService(new MessengerSender());
        messengerService.sendNotification("Hello via messenger");
    }
}
