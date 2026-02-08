package Order;
interface Notifier {
    void sendConfirmation(String destination, String message);
}