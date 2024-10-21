import java.time.LocalDate;
import java.util.Scanner;

public class Reception {
    boolean userNotFoundCheck=false;
    boolean userActive=false;

    public void receptionMenu(){
        String id;
        String choice;
        DataHandler dh = new DataHandler();
        Scanner scan = new Scanner(System.in);
        System.out.println("Välkommen till menyn!\nAnge det första ordet som svarsalternativ:\n" +
                "SÖK: Söka efter en användare\n" +
                "AVSLUTA: Avsluta programmet");
        choice=scan.nextLine().toUpperCase();

        switch(choice){
            case "SÖK":
                System.out.println("Vänligen ange den identifiering du vill söka efter:");
                id=scan.nextLine();
                try{
                    id=dh.capitalizer(id);
                    dh.scanForUser(id, dh.pathGetterUserList(), false);
                    }
                catch(Exception e){
                    System.exit(0);
                }
                break;
            case "AVSLUTA":
                System.out.println("Programmet avslutas");
                System.exit(0);
                break;
            default:
                System.out.println("Inget giltigt alternativ angivet, programmet avslutas");
                System.exit(0);

        }
        }

    public void subscriptionInfo(LocalDate date, String name, boolean active, boolean test){
        LocalDate subscriptionExpires = date.plusYears(1);
        StringBuilder userMessage=new StringBuilder();
        if (active){
            userActive=true;
            userMessage.append("Aktiv! ")
            .append(name)
            .append(" har ett medlemskap t.o.m datum: ")
            .append(subscriptionExpires);
            System.out.println(userMessage);
            if(!test){
            receptionMenu();}
        }
        if(!active){
            userActive=false;
            userMessage.append("Ej aktiv! Användaren ")
            .append(name)
            .append(" medlemskap utgick datum: ")
            .append(subscriptionExpires);
            System.out.println(userMessage);
            if (!test){
            receptionMenu();}
        }
    }

    public void userNotFound (String id, boolean test){
        userNotFoundCheck=true;
        System.out.println("Ingen användare med identifieringen: "+id+" kunde hittas i systemet.");
        if(!test){
        receptionMenu();}
    }
}
