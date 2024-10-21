import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataHandler {
    Reception reception = new Reception();
    LocalDate todaysDate = LocalDate.now();
    boolean foundInScan = false;
    boolean isActive=false;
    private Path p1 = Paths.get("src/UserData.txt");
    private Path p2 = Paths.get("src/UserLog.txt");


    public Path pathGetterUserList(){
        return p1;
    }
    public Path pathGetterLog(){
        return p2;
    }

    public String capitalizer(String name) {
        StringBuilder fullName = new StringBuilder();
        String[] words = name.split("\\s");
        for (String word : words) {
            fullName.append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return fullName.toString().trim();
    }

    public void scanForUser(String id, Path p, boolean test) throws IOException {

        foundInScan=false;
        try {
            if (!Files.exists(p)) {
                Files.createFile(p);
            }}
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        //Testar att läsa in filen från Path p
        try (Scanner sc = new Scanner(p)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //splittar till CSV, datumet ignoreras därför automatiskt
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    //delar upp person numret och det fullständiga namnet till två olika delar
                    String idNumber = parts[0];
                    String name = parts[1].trim();
                    // kollar igenom för varje loop om det är en matchning
                    if (name.equals(id) || idNumber.equals(id)) {
                        //det är en match och datumet hämtas ut
                        foundInScan=true;
                        String date = sc.nextLine();
                        //skickas till datumskoll
                        if (test){
                            dateChecker(date, name, idNumber,true);
                            break;
                        }
                        if(!test){
                            dateChecker(date, name, idNumber,false);
                            break;
                            }
                    }
                }

            }
        }

        catch (IOException e) {
            System.out.println((e.getMessage()));
            throw new IOException(e);
        }

        if (!foundInScan&&!test) {
            reception.userNotFound(id,false);

        }
        if(!foundInScan&&test) {
            reception.userNotFound(id,true);
        }

    }

    public void dateChecker(String date, String name,String id, boolean test) throws IOException {
        LocalDate dateOfUserSubscription = null;
        isActive=false;
        try {
            dateOfUserSubscription = LocalDate.parse(date);
            if (dateOfUserSubscription.isAfter(todaysDate.minusYears(1))) {
                isActive=true;
                if (!test){
                    logUserCheckIn(name, id, pathGetterLog());
                    reception.subscriptionInfo(dateOfUserSubscription, name, true, false);}
                if(test){
                    reception.subscriptionInfo(dateOfUserSubscription, name, true, true);
                }
            }
            else {
                if (!test){
                reception.subscriptionInfo(dateOfUserSubscription, name, false,false);}
                if (test){
                    reception.subscriptionInfo(dateOfUserSubscription, name, false, true);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Fel inmatning av datum från text dokumentet");
            System.out.println(e.getMessage());
            throw new IOException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (date.equals("triggerRuntimeException")) {
            throw new RuntimeException("Triggat error av testsyfte");
        }

    }

    public void logUserCheckIn(String name, String id, Path p2) throws IOException {
        List<String> lines;
        foundInScan = false;
        int lineIndex = -1;

        try {
            if (!Files.exists(p2)) {
                Files.createFile(p2);
            }
            lines = Files.readAllLines(p2);
        } catch (IOException e) {
            System.out.println("Något gick fel när filen skulle läsas eller skapas");
            throw new RuntimeException(e);
        }

        // Söker efter om användarID redan finns i dokumentet, och vart
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length > 1 && parts[0].equals(id)) {
                foundInScan = true;
                lineIndex = i;
                break;
            }
        }

        // uppdaterar listan med nya info som ska skrivas in
        if (foundInScan) {
            // användaren finns, dagens datum läggs in i raden direkt under namnet
            lines.add(lineIndex + 1, todaysDate.toString());
        } else {
            // Ej tidigare loggad, läggs till längst ner i listan med id, namn och datum
            lines.add(id + "," + name);
            lines.add(todaysDate.toString());
        }

        // skriver in listan till dokumentet
        try {
            Files.write(p2, lines);
        } catch (IOException e) {
            System.out.println("Något gick fel när filen skulle skrivas");
            throw new IOException(e);
        }
    }
}


