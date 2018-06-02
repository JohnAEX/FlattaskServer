package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class IdFetcher extends Thread {
    private Databasehandler localHandler;

    private String prefix;
    private int idCoreLength;
    private String lowerCoreBoundary;
    private String upperCoreBoundary;
    private int untilAmount;
    private ArrayList<String> idStore;
    private boolean idDepletion;

    public IdFetcher(String prefix, int idCoreLength, String lowerCoreBoundary, String upperCoreBoundary,
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

    public String getPrefix()
    {
        return prefix;
    }
}
