package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PreDestroy;
import java.util.ArrayList;

class CommunityIdFetcher extends Thread {
    private final int AMOUNT;
    private final int DIGITS_TO_WRITE_TO;
    private final int ID_CORE_LENGTH;
    private final String PREFIX;
    private final ArrayList<String> ID_STORE;
    private final Databasehandler LOCAL_HANDLER = Databasehandler.instanceOf();

    public CommunityIdFetcher(int amount, int digitsToWriteTo, int idCoreLength, String prefix, ArrayList<String> idStore)
    {
        this.AMOUNT = amount;
        this.DIGITS_TO_WRITE_TO = digitsToWriteTo;
        this.ID_CORE_LENGTH = idCoreLength;
        this.PREFIX = prefix;
        this.ID_STORE = idStore;
        this.LOCAL_HANDLER.initObjectDBConnection();
    }

    @Override
    public void run()
    {
        // Remember for difference
        final int maxCacheLength = this.ID_STORE.size() + this.AMOUNT;

        // Count for AMOUNT
        final int maxId = this.DIGITS_TO_WRITE_TO * 10 - 1;
        for(int i = 0; i <= maxId; ++i) {
            // Generate fixed-width Id
            String checkId = this.PREFIX + StringUtils.leftPad(String.valueOf(i), this.ID_CORE_LENGTH, '0');
            if(this.LOCAL_HANDLER.getCommunityById(checkId) == null) {
                // Id is free
                this.ID_STORE.add(checkId);

                if(this.ID_STORE.size() == maxCacheLength) {
                    // Preferred break condition: IdStore is full
                    return;
                }
            }
            // Unpreferred break condition: all Ids have been checked, none are left (and IdStore is not full)
        }
    }
}
