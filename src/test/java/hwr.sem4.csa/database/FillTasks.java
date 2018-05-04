package hwr.sem4.csa.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FillTasks {

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
        
    }
}
