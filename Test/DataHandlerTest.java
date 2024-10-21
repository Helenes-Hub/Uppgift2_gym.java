
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class DataHandlerTest {

    Path pTest = Paths.get("src/TestData.txt");
    LocalDate todaysDate = LocalDate.now();
    DataHandler dh = new DataHandler();

    @Test
    void capitalizer() {
        String[] testNames={"Lisa ANNA", "ANNA MARIE", "GÖRAN PER ANDERSSON", "per olov jönsson","1234"};
        String[] testNamesFixed={"Lisa Anna", "Anna Marie", "Göran Per Andersson","Per Olov Jönsson","1234"};
        for (int i=0; i<testNames.length; i++) {
            assert(Objects.equals(dh.capitalizer(testNames[i]), testNamesFixed[i]));
            assert (!Objects.equals(dh.capitalizer(testNames[i]), "hubert"));
        }
    }

    @Test
    void scanForUser() throws IOException {
        String contentToWrite =
                "123,Aktiv Person\n" +
                 todaysDate + "\n" +
                 "456,Inaktiv Person\n" +
                 todaysDate.minusYears(2);
        Files.writeString(pTest, contentToWrite);
        dh.scanForUser("123", pTest,true);
        assert(dh.foundInScan);
        assert(dh.isActive);
        dh.scanForUser("456",pTest,true);
        assert(dh.foundInScan);
        assert(!dh.isActive);
        dh.scanForUser("789", pTest,true);
        assert(!dh.foundInScan);
        assert(!dh.isActive);
        Path invalidPath = Paths.get("/invalid/directory/UserData.txt");
        assertThrows(IOException.class, () -> {
            dh.scanForUser("123", invalidPath, true);
        });
        Files.delete(pTest);
        assert(!Files.exists(pTest));

    }

    @Test
    void dateChecker() throws IOException {

        dh.dateChecker(String.valueOf(todaysDate), "Aktiv Person", "123", true);
        assert(dh.isActive);
        dh.dateChecker(String.valueOf(todaysDate.minusYears(2)), "Inaktiv Person", "456", true);
        assert(!dh.isActive);

        assertThrows(IOException.class, () -> {
            dh.dateChecker("invalidId", "abc","000", true);
        });
        assertThrows(IOException.class, () -> {
            dh.dateChecker("triggerRuntimeException", "def","111", true);
        });
    }

    @Test
    void logUserCheckIn() throws IOException {
        dh.logUserCheckIn("Test Person1", "1111", pTest);
        assert(!dh.foundInScan);
        dh.logUserCheckIn("Test Person2", "2222", pTest);
        assert(!dh.foundInScan);
        dh.logUserCheckIn("Test Person3", "3333", pTest);
        assert(!dh.foundInScan);
        dh.logUserCheckIn("Test Person1", "1111", pTest);
        assert(dh.foundInScan);
        dh.logUserCheckIn("Test Person2", "2222", pTest);
        assert(dh.foundInScan);

        List<String> lines = Files.readAllLines(pTest);
        List<String> expectedLines = List.of(
                "1111,Test Person1",
                todaysDate.toString(),
                todaysDate.toString(),
                "2222,Test Person2",
                todaysDate.toString(),
                todaysDate.toString(),
                "3333,Test Person3",
                todaysDate.toString()
        );

        assert(expectedLines.equals(lines));
        Files.delete(pTest);
        assert(!Files.exists(pTest));

        }

    }

