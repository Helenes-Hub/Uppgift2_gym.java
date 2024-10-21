import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

public class ReceptionTest {


        Reception reception=new Reception();
        LocalDate todaysDate = LocalDate.now();



    @Test
    void receptionMenu() {

    }

    @Test
    void subscriptionInfo() {
        reception.subscriptionInfo(todaysDate,"Active User", true, true);
        assert(reception.userActive);
        reception.subscriptionInfo(todaysDate,"Inactive User", false, true);
        assert(!reception.userActive);
        reception.subscriptionInfo(todaysDate,"Active User", true, true);
        assert(reception.userActive);
        reception.subscriptionInfo(todaysDate,"Inactive User", false, true);
        assert(!reception.userActive);

    }

    @Test
    void userNotFound() {
        assert(!reception.userNotFoundCheck);
        reception.userNotFound("Ej hittat id1", true);
        assert(reception.userNotFoundCheck);
        reception.userNotFound("Ej hittat id2", true);
        assert(reception.userNotFoundCheck);
        reception.userNotFound("Ej hittat id3", true);
        assert(reception.userNotFoundCheck);
    }
}
