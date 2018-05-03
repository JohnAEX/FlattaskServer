package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
        /*
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
        */

        //Testing ODB with ArrayLists
        /*
        Community c2 = new Community();
        c2.setId("ArrayC2");
        c2.setName("ArrayCommunityTest");
        c2.setCreationTime("NOW");
        ArrayList<Dotos> dotosList = new ArrayList<>();
        ArrayList<Task> tasksList = new ArrayList<>();
        Dotos doto = new Dotos();
        doto.setTitle("Title 1");
        Dotos doto2 = new Dotos();
        doto2.setTitle("Title 2");

        dotosList.add(doto);
        dotosList.add(doto2);

        c2.setDotosList(dotosList);

        Task t1 = new Task();
        Task t2 = new Task();

        tasksList.add(t1);
        tasksList.add(t2);

        c2.setTaskList(tasksList);

        Databasehandler.instanceOf().insert(c2);

        Community dbC = Databasehandler.instanceOf().getCommunityById(c2.getId());
        ArrayList<Dotos> dbDotoList = dbC.getDotosList();
        Assertions.assertTrue(dbDotoList.get(0).title.equals("Title 1"));

        dbDotoList.get(0).setTitle("REWORKED");

        Databasehandler.instanceOf().removeCommunityById(dbC.getId());

        Community newC = new Community();
        newC.setId(dbC.getId());
        newC.setName(dbC.getName());
        newC.setCreationTime(dbC.getCreationTime());
        newC.setDotosList(dbDotoList);
        newC.setTaskList(dbC.getTaskList());

        Databasehandler.instanceOf().insert(newC);

        Community dbC2 = Databasehandler.instanceOf().getCommunityById(c2.getId());
        ArrayList<Dotos> dbDotoList2 = dbC2.getDotosList();
        Assertions.assertTrue(dbDotoList2.get(0).title.equals("REWORKED"));*/
    }
}
