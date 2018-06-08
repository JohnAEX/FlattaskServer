package hwr.sem4.csa.management;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static hwr.sem4.csa.management.IdManagementConstants.COMMUNITY_MGT_DEFAULTS_FILE;
import static hwr.sem4.csa.management.IdManagementConstants.MAX_NEW_COMMUNITIES_PER_DAY;

public class IdManager extends IdManagementCore {
    // Singleton
    private static IdManager thisIdManager = new IdManager();
    public static IdManager getInstance()
    {
        return thisIdManager;
    }

    private IdManagementCore corePropertiesCIds;
    private ArrayList<String> cIdCache;

    private CommunityIdFetcher cIdFetcher;

    private IdManager()
    {
        // Load JSON Files
        this.corePropertiesCIds = coreFromFile(COMMUNITY_MGT_DEFAULTS_FILE);
        this.cIdCache = new ArrayList<String>();

        // Initial fetch
        this.cIdFetcher = generateNewFetcher();
        this.cIdFetcher.start();
        this.getFreeCId();
        try {
            this.cIdFetcher.join();
        } catch(InterruptedException iExc) {
            System.out.println("Fetch process has been interrupted.");
        }
        System.out.println("IdManager construction complete");
    }

    public String getFreeCId()
    {
        if(this.cIdCache.size() == 0) {
            // Cache empty
            try{
                this.cIdFetcher.join();
            } catch(InterruptedException iExc) {
                // Logging will be implemented
                return null;
            }
            if(this.cIdCache.size() == 0) {
                // No free Ids
                return null;
            }
        }

        // Grab free Id
        String fetchedId = this.cIdCache.get(0);
        this.cIdCache.remove(0);

        if(this.cIdCache.size() < this.corePropertiesCIds.getMinIdCacheLength()) {
            // Lower cache limit has been trespassed
            if(this.cIdFetcher.getState() == Thread.State.TERMINATED) {
                // Only restart fetcher if last fetch has finished
                this.cIdFetcher = generateNewFetcher();
                // Start fetching new Ids
                this.cIdFetcher.start();
            }
        }

        return fetchedId;
    }

    private CommunityIdFetcher generateNewFetcher() {
        // Get fetch parameters
        int fetchAmount = this.corePropertiesCIds.getMaxIdCacheLength() - this.cIdCache.size();
        int digitsToWriteTo = getDigitCount(this.corePropertiesCIds.getNumberOfDailyIds());
        int idCoreLength = getDigitCount(MAX_NEW_COMMUNITIES_PER_DAY);
        String fullPrefix = this.corePropertiesCIds.getPrefix() + "-" + this.corePropertiesCIds.getDateFormat().format(new Date()) + "-";

        // Fetch free Ids
        return new CommunityIdFetcher(fetchAmount, digitsToWriteTo, idCoreLength, fullPrefix, this.cIdCache);
    }

    private static IdManagementCore coreFromFile(String filePath)
    {
        // Load default configs
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inFromFile = loader.getResourceAsStream(filePath);
        try {
            return new ObjectMapper().readValue(inFromFile, IdManagementCore.class);
        } catch(IOException ioExc) {
            // Log this
            throw new IllegalArgumentException("Could not read config file \"" + COMMUNITY_MGT_DEFAULTS_FILE + "\": " + ioExc.getMessage());
        }
    }

    /* User "UserNotFound"s answer to: https://stackoverflow.com/questions/1306727/way-to-get-number-of-digits-in-an-int */
    private static int getDigitCount(int n)
    {
        return (n<100000)?((n<100)?((n<10)?1:2):(n<1000)?3:((n<10000)?4:5)):((n<10000000)?((n<1000000)?6:7):((n<100000000)?8:((n<1000000000)?9:10)));
    }
}