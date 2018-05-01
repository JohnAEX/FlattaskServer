package hwr.sem4.csa.database;

import hwr.sem4.csa.managedBeans.LoginManagedBean;
import hwr.sem4.csa.util.Participator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginTest {

    @BeforeAll
    public static void runBeforeAll(){
        Databasehandler.instanceOf().initObjectDBConnection();
    }

    @AfterAll
    public static void runAfterAll(){
        Databasehandler.instanceOf().close();
    }

    @Test
    public void setupAccount(){
        Participator testParticipator = new Participator();
        testParticipator.setUsername("JohnDoe");
        testParticipator.setBalance(100);
        testParticipator.setRole("user");
        testParticipator.setFirstName("John");
        testParticipator.setLastName("Doe");
        testParticipator.setPassword("User1234");
        Databasehandler.instanceOf().insert(testParticipator);
    }

    @Test
    public void login(){
        String username = "JohnDoe";
        String password = "User1234";
        System.out.println("Attempted Login for: " + username + " - " + password);
        Assertions.assertNotNull(Databasehandler.instanceOf().getParticipatorByLogin(username,password));
    }

    @Test
    public void setToAdmin(){
        Databasehandler.instanceOf().updateParticipator("JohnDoe","User1234","John","Doe",100,"admin","NONE","");
    }
}
