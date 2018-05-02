package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DotoReader {

    @BeforeAll
    public static void runBeforeAll(){
        Databasehandler.instanceOf().initObjectDBConnection();
    }

    @AfterAll
    public static void runAfterAll(){
        Databasehandler.instanceOf().close();
    }

    @Test
    public void dotoReaderTest(){
        /*
        Dotos d1 = new Dotos();
        Dotos d2 = new Dotos();

        d1.setId(42);
        d2.setId(69);

        ArrayList<Dotos> dList = new ArrayList<Dotos>();
        dList.add(d1);
        dList.add(d2);

        Community cTest = new Community();
        cTest.setId("TESTCOMMUNITY");
        cTest.setDotosList(dList);

        Databasehandler.instanceOf().insert(cTest);

        ArrayList<Dotos> dbList = Databasehandler.instanceOf().getCommunityById("TESTCOMMUNITY").getDotosList();
        Assertions.assertTrue(dbList.get(dbList.size()-1).getId() == 69);
        */
    }
}
