package hwr.sem4.csa.management;

import hwr.sem4.csa.comparators.DotosComparator;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Iterator;

public class DotoIdFetcher extends Thread {
    private final String communityId;
    private final int amount;
    private final int upperLimit;
    private final ArrayList<Integer> idStore;
    private final Databasehandler localHandler = Databasehandler.instanceOf();

    public DotoIdFetcher(String communityId, int amount, int upperLimit, ArrayList<Integer> idStore) {
        this.communityId = communityId;
        this.amount = amount;
        this.upperLimit = upperLimit;
        this.idStore = idStore;
        this.localHandler.initObjectDBConnection();
    }

    @Override
    public void run()
    {
        // Fetch current standings
        Community aimCommunity = this.localHandler.getCommunityById(this.communityId);
        ArrayList<Dotos> aimDotoList = new ArrayList<Dotos>(aimCommunity.getDotosList());

        final int targetStoreLength = this.idStore.size() + this.amount;

        // Sort DotoList
        aimDotoList.sort(new DotosComparator());

        // Search for in-between open Ids
        int nextId = 0;
        for(Iterator<Dotos> dotoIt = aimDotoList.iterator(); dotoIt.hasNext(); ) {
            // Check for limit-trespassing
            if(nextId == upperLimit) {
                return;
            }

            int currentDotoId = dotoIt.next().getId();

            // Check for left-out ("free") Ids
            if(currentDotoId > nextId) {
                // Grab all left-out Ids
                for(int i = nextId; i < currentDotoId; ++i) {
                    this.idStore.add(i);

                    if(this.idStore.size() == targetStoreLength) {
                        // Preferred break condition: IdStore is full
                        return;
                    }

                    // Check for limit-trespassing
                    if(i == this.upperLimit) {
                        return;
                    }
                }
            }

            // Assign anticipated next Id
            nextId = currentDotoId + 1;
        }

        // Search for further Ids if in-between Ids did not suffice
        while(nextId < upperLimit) {
            this.idStore.add(nextId);
            if(this.idStore.size() == targetStoreLength) {
                // Preferred break condition: IdStore is full
                return;
            }
            ++nextId;
        }

        // If path execution reaches this point, the idStore couldn't be filled fully
    }

    @PreDestroy
    public void cleanUp()
    {
        this.localHandler.close();
    }

}
