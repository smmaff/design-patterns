package homework3.ISP;

interface Printable {
    void print(String content);
}

interface Scannable {
    void scan(String content);
}

interface Faxable {
    void fax(String content);
}

class AllInOnePrinter implements Printable, Scannable, Faxable {
    @Override
    public void print(String content) {
        System.out.println("Printing: " + content);
    }

    @Override
    public void scan(String content) {
        System.out.println("Scanning: " + content);
    }

    @Override
    public void fax(String content) {
        System.out.println("Faxing: " + content);
    }
}

class BasicPrinter implements Printable {
    @Override
    public void print(String content) {
        System.out.println("Printing: " + content);
    }
}

class OfficePrinter implements Printable, Scannable {
    @Override
    public void print(String content) {
        System.out.println("Printing: " + content);
    }

    @Override
    public void scan(String content) {
        System.out.println("Scanning: " + content);
    }
}

public class IspDemo {
    public static void main(String[] args) {
        Printable p1 = new BasicPrinter();
        p1.print("Hello");

        AllInOnePrinter p2 = new AllInOnePrinter();
        p2.print("Doc");
        p2.scan("Doc");
        p2.fax("Doc");
    }
}
