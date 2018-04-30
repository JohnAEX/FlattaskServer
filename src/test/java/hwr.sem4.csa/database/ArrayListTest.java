package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ArrayListTest {

    @BeforeAll
    public static void runBeforeAll(){
        Databasehandler.instanceOf().initObjectDBConnection();
    }

    @AfterAll
    public static void runAfterAll(){
        Databasehandler.instanceOf().close();
    }

    @Test
    public void testArrayPersist(){
        Community c = new Community();
        c.setId("TestID4");
        ArrayList<Dotos> myArrayList = new ArrayList();
        Dotos myDoto1 = new Dotos();
        myDoto1.setId(1);
        Dotos myDoto2 = new Dotos();
        myDoto2.setId(2);
        myArrayList.add(myDoto1);
        myArrayList.add(myDoto2);
        c.setDotosList(myArrayList);

        Databasehandler.instanceOf().insert(c);
    }
}
