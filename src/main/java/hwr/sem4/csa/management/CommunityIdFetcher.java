package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PreDestroy;
import java.util.ArrayList;

class CommunityIdFetcher extends Thread {
    private final int amount;
    private final int idCoreLength;
    private final String prefix;
    private final ArrayList<String> idStore;
    private final Databasehandler localHandler = Databasehandler.instanceOf();

    public CommunityIdFetcher(int amount, int idCoreLength, String prefix, ArrayList<String> idStore)
    {
        this.amount = amount;
        this.idCoreLength = idCoreLength;
        this.prefix = prefix;
        this.idStore = idStore;
        this.localHandler.initObjectDBConnection();
    }

    @Override
    public void run()
    {
        // Remember for difference
        final int initialIdStoreLength = this.idStore.size();

        // Count for amount
        final int maxId = this.idCoreLength * 10 - 1;
        for(int i = 0; i <= maxId; ++i) {
            // Generate fixed-width Id
            String checkId = this.prefix + StringUtils.leftPad(String.valueOf(i), this.idCoreLength, '0');
            if(this.localHandler.getCommunityById(checkId) == null) {
                // Id is free
                this.idStore.add(checkId);

                if(this.idStore.size() == initialIdStoreLength + this.amount) {
                    // Preferred break condition: IdStore is full
                    return;
                }
            }
            // Unpreferred break condition: all Ids have been checked, none are left (and IdStore is not full)
        }
    }

    @PreDestroy
    public void cleanUp()
    {
        this.localHandler.close();
    }

}
