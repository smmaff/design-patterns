package Order;
class EmailNotifier implements Notifier {
    @Override
    public void sendConfirmation(String destination, String message) {
        System.out.println("Confirmation email sent to: " + destination + " | " + message);
    }
}