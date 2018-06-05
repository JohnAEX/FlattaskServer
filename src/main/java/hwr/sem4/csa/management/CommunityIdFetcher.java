package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.exceptions.OutOfIdsException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

class CommunityIdFetcher extends Thread {
    private Databasehandler localHandler;

    private String prefix;
    private int idCoreLength;
    private String lowerCoreBoundary;
    private String upperCoreBoundary;
    private int untilAmount;
    private ArrayList<String> idStore;
    private boolean idDepletion;

    CommunityIdFetcher(String prefix, int idCoreLength, String lowerCoreBoundary, String upperCoreBoundary,
                       int untilAmount, ArrayList<String> idStore, boolean idDepletion)
    {
        this.localHandler = Databasehandler.instanceOf();

        this.prefix = prefix;
        this.idCoreLength = idCoreLength;
        this.lowerCoreBoundary = lowerCoreBoundary;
        this.upperCoreBoundary = upperCoreBoundary;
        this.untilAmount = untilAmount;
        this.idStore = idStore;
        this.idDepletion = idDepletion;
    }

    @Override
    public void run()
    {
        localHandler.initObjectDBConnection();
        String checkId = this.prefix;
        for(BigInteger idCounter = new BigInteger(lowerCoreBoundary, 16); idStore.size() < untilAmount; idCounter = idCounter.add(BigInteger.ONE)) {
            checkId = checkId.substring(0, 11) + StringUtils.leftPad(idCounter.toString(), this.idCoreLength, '0');

            //Adding an unused Id
            if(localHandler.getCommunityById(checkId) == null && !this.idStore.contains(checkId)) {
                idStore.add(checkId);
                this.idDepletion = false;
            }

            //checking whether Id stock is exhausted
            if(idCounter.compareTo(new BigInteger(upperCoreBoundary, 16)) >= 0 && idStore.size() == 0) {
                this.idDepletion = true;
            }
        }
        Collections.sort(this.idStore);
        localHandler.close();
    }


    static String smartFetch(CommunityIdFetcher fetcher, IdManagementCore coreProperties, String lowerCoreBoundary, String upperCoreBoundary,
                             ArrayList<String> idStore, boolean idDepletion)
            throws OutOfIdsException
    {
        // Wait for CommunityIdFetcher to finish if Cache has trespassed lower limit
        if(idStore.size() < coreProperties.getMinIdCacheLength()) {
            try{
                fetcher.join();
            } catch(InterruptedException iExc) {
                // Log this
            }
        }

        // Break if no Ids are available
        if(idDepletion) {
            throw new OutOfIdsException("All Ids have been consumed for the date " + coreProperties.getDateFormat().format(new Date()) + ". Try again later.");
        }

        // Grab unused Id
        String freeId = idStore.get(0);
        idStore.remove(0);

        // Fetch Ids if Cache has trespassed lower limit
        if(idStore.size() < coreProperties.getMinIdCacheLength()) {
            fetcher = new CommunityIdFetcher(coreProperties.getPrefix() + "-" + coreProperties.getDateFormat().format(new Date()) + "-", coreProperties.getIdCoreLength(),
                    lowerCoreBoundary, upperCoreBoundary, coreProperties.getMaxIdCacheLength(), idStore, idDepletion);
            fetcher.start();
        }

        return freeId;
    }
}
