package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.exceptions.OutOfIdsException;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommunityIdManager {
    private static CommunityIdManager thisCommunityIdManager = new CommunityIdManager();

    private Databasehandler localHandler = Databasehandler.instanceOf();
    private DateFormat dateFormat;
    private String prefix;
    private int idCoreLength;
    private String lowerCoreBoundary;
    private String upperCoreBoundary;
    private boolean idDepletion;
    private int minIdCacheLength;
    private int maxIdCacheLength;
    private ArrayList<String> nextFreeIds;

    private IdFetcher idFetcher;

    public static CommunityIdManager getInstance()
    {
        return thisCommunityIdManager;
    }

    private CommunityIdManager()
    {
        this.localHandler.initObjectDBConnection();
        this.dateFormat = new SimpleDateFormat("yyyyMMdd");
        this.prefix = "C";
        this.idCoreLength = 9;
        this.lowerCoreBoundary = StringUtils.leftPad("", this.idCoreLength, '0');
        this.upperCoreBoundary = StringUtils.leftPad("", this.idCoreLength, 'F');
        this.minIdCacheLength = 15;
        this.maxIdCacheLength = 20;
        this.nextFreeIds = new ArrayList<String>();
        //Initial search for free Ids
        this.idFetcher = new IdFetcher("initialFetcher", dateFormat.format(new Date()), this.prefix, this.idCoreLength, this.lowerCoreBoundary,
                this.upperCoreBoundary, this.maxIdCacheLength, this.nextFreeIds, this.idDepletion);
        this.idFetcher.start();
        try {
            this.idFetcher.join();
        }catch (InterruptedException iExc) {
            System.out.println("The Thread " + this.idFetcher.toString() + " has been interrupted");
        }
    }

    public String getNewId() throws OutOfIdsException
    {
        //Check for current Date
        if(!this.idFetcher.getCurrentDate().equals(dateFormat.format(new Date()))) {
            this.nextFreeIds.clear();
            this.idFetcher.setCurrentDate(dateFormat.format(new Date()));
            if(this.idFetcher.isAlive()) {
                this.idFetcher.interrupt();
                this.idFetcher.setCurrentDate(this.idFetcher.getCurrentDate());
            }
        }

        if(this.nextFreeIds.size() < this.minIdCacheLength) {
            this.idFetcher.start();
        }

        //Check for existence of free Ids
        if(this.idDepletion) {
            throw new OutOfIdsException("All Ids have been taken for the date " + this.idFetcher.getCurrentDate());
        }

        String freeId = this.nextFreeIds.get(0);
        this.nextFreeIds.remove(0);
        return freeId;
    }


}
