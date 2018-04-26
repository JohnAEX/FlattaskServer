package hwr.sem4.csa.database;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Participator;
import org.junit.jupiter.api.*;

public class DatabasehandlerTest {

    @BeforeAll
    public static void runBeforeAll(){
        Databasehandler.instanceOf().initObjectDBConnection();
        //Databasehandler.instanceOf().clearAll();
    }

    @AfterAll
    public static void runAfterAll(){
        Databasehandler.instanceOf().clearAll();
        Databasehandler.instanceOf().close();
    }

    @Test
    public void getCommunityByIdTest(){
        Community testCommunity = new Community();
        testCommunity.setCreationTime("12.04.2018");
        testCommunity.setId("TESTID");
        testCommunity.setDotosList(null);
        testCommunity.setTaskList(null);

        //Databasehandler.instanceOf().initObjectDBConnection();
        Databasehandler.instanceOf().insert(testCommunity);
        Community secondCommunity = Databasehandler.instanceOf().getCommunityById("TESTID");
        Assertions.assertTrue(secondCommunity.getId().equals(testCommunity.getId()));
    }

    @Test
    public void getParticipatorByUsername(){
        Participator testParticipator = new Participator();
        testParticipator.setUsername("TESTUSERNAME");
        testParticipator.setBalance(100);
        testParticipator.setRole("user");
        testParticipator.setFirstName("TESTFIRSTNAME");

        Databasehandler.instanceOf().insert(testParticipator);

        Participator secondParticipator = Databasehandler.instanceOf().getParticipatorByUsername("TESTUSERNAME");
        Assertions.assertTrue(secondParticipator.getFirstName().equals(testParticipator.getFirstName()));
    }

    @Test
    public void getParticipatorByLogin(){
        Participator testParticipator = new Participator();
        testParticipator.setUsername("TESTUSERNAME2");
        testParticipator.setBalance(100);
        testParticipator.setRole("user");
        testParticipator.setFirstName("TESTFIRSTNAME2");
        testParticipator.setPassword("TESTPASSWORD2");

        Databasehandler.instanceOf().insert(testParticipator);

        Participator secondParticipator = Databasehandler.instanceOf().getParticipatorByLogin("TESTUSERNAME2","TESTPASSWORD2");
        Assertions.assertTrue(secondParticipator.getUsername().equals(testParticipator.getUsername()));
        Assertions.assertTrue(secondParticipator.getPassword().equals(testParticipator.getPassword()));

    }


}
