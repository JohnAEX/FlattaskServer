package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.DummyCreation;
import hwr.sem4.csa.util.Participator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SystemTest {

    @BeforeAll
    public static void runBeforeAll(){
        Databasehandler.instanceOf().initObjectDBConnection();
    }

    @AfterAll
    public static void runAfterAll(){
        Databasehandler.instanceOf().close();
    }

    @Test
    public void fillDB(){
        List<Participator> dummyList = DummyCreation.instanceOf().createDummyParticipators(10);
        for(Participator p: dummyList){
            Databasehandler.instanceOf().insert(p);
        }
    }

    @Test
    public void clearDB_AndInsertJohnDoe(){
        Databasehandler.instanceOf().clearAll();
        Participator testParticipator = new Participator();
        testParticipator.setUsername("JohnDoe");
        testParticipator.setBalance(100);
        testParticipator.setRole("admin");
        testParticipator.setFirstName("John");
        testParticipator.setLastName("Doe");
        testParticipator.setPassword("User1234");
        Databasehandler.instanceOf().insert(testParticipator);
    }

    @Test
    public void setup(){
        clearDB_AndInsertJohnDoe();
        List<List> newList = DummyCreation.instanceOf().createUsableData(10);
        List<Participator> pList = newList.get(0);
        List<Community> cList = newList.get(1);
        Databasehandler.instanceOf().insertList(pList);
        Databasehandler.instanceOf().insertList(cList);
    }
}
