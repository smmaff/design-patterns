package practice4.DocumentApp;
import java.util.Scanner;

interface Document{
    void open();
}
class Report implements Document{
    @Override
    public void open(){
        System.out.println("Opening report document");
    }
}
class Resume implements Document{
    @Override
    public void open(){
        System.out.println("Opening resume document");
    }
}
class Letter implements Document{
    @Override
    public void open(){
        System.out.println("Opening letter document");
    }
}
class Invoice implements Document{
    @Override
    public void open(){
        System.out.println("Opening invoice document");
    }
}
abstract class DocumentCreator{
    public abstract Document createDocument();
}
class ReportCreator extends DocumentCreator{
    @Override
    public Document createDocument(){
        return new Report();
    }
}
class ResumeCreator extends DocumentCreator{
    @Override
    public Document createDocument(){
        return new Resume();
    }
}
class LetterCreator extends DocumentCreator{
    @Override
    public Document createDocument(){
        return new Letter();
    }
}
class InvoiceCreator extends DocumentCreator{
    @Override
    public Document createDocument(){
        return new Invoice();
    }
}
public class DocumentApp{
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter document type: report, resume, letter, invoice");
        String type=scanner.nextLine().trim().toLowerCase();
        DocumentCreator creator;
        switch(type){
            case "report":
                creator=new ReportCreator();
                break;
            case "resume":
                creator=new ResumeCreator();
                break;
            case "letter":
                creator=new LetterCreator();
                break;
            case "invoice":
                creator=new InvoiceCreator();
                break;
            default:
                System.out.println("Unknown document type");
                scanner.close();
                return;
        }
        Document document=creator.createDocument();
        document.open();
        scanner.close();
    }
}
