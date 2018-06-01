package hwr.sem4.csa.manual;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.exceptions.OutOfIdsException;
import hwr.sem4.csa.management.CommunityIdManager;
import hwr.sem4.csa.util.Community;

import java.util.Scanner;

public class addCommunities {

    public static void main(String[] args)
    {
        Scanner sa = new Scanner(System.in);
        CommunityIdManager myComMgr = CommunityIdManager.getInstance();

        Databasehandler localHandler = Databasehandler.instanceOf();
        localHandler.initObjectDBConnection();

        boolean quit = false;

        while(!quit) {
            System.out.print("Number of communities: ");
            int amount = sa.nextInt();
            System.out.print("Community prefix: ");
            String prefix = sa.next();

            insertThat(localHandler, myComMgr, amount, prefix);

            System.out.println("Continue = y");
            quit = !sa.next().equals("y");
        }

    }

    private static void insertThat(Databasehandler localHandler, CommunityIdManager myComMgr, int amount, String prefix)
    {
        try {
            for(int i = 0; i < amount; ++i) {
                String newId = myComMgr.getNewId();
                Community tempCom = new Community(newId, prefix + "-" + i, "Jetzt");
                localHandler.insert(tempCom);
            }
        }catch(OutOfIdsException ooiExc) {
            System.out.println("No Ids left");
        }
    }
}
