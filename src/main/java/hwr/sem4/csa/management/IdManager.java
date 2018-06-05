package hwr.sem4.csa.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwr.sem4.csa.exceptions.OutOfIdsException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static hwr.sem4.csa.management.IdManagementConstants.COMMUNITY_MGT_DEFAULTS_FILE;
import static hwr.sem4.csa.management.IdManagementConstants.DOTO_MGT_DEFAULTS_FILE;

public class IdManager extends IdManagementCore {
    // Singleton
    private static IdManager thisIdManager = new IdManager();
    public static IdManager getInstance()
    {
        return thisIdManager;
    }

    private IdManagementCore corePropertiesCIds;
    private IdManagementCore corePropertiesDIds;
    private ArrayList<String> cIdCache;

    private CommunityIdFetcher cIdFetcher;

    private IdManager()
    {
        // Load JSON Files
        this.corePropertiesCIds = coreFromFile(COMMUNITY_MGT_DEFAULTS_FILE);
        this.corePropertiesDIds = coreFromFile(DOTO_MGT_DEFAULTS_FILE);

        // Initial fetch
        this.getFreeCId();
    }

    public String getFreeCId()
    {
        if(this.cIdCache.size() >= corePropertiesCIds.getMinIdCacheLength()) {
            // Sufficient Ids present
            return this.cIdCache.get(0);
        }
        if(this.cIdCache.size() == 0) {
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

        // Get fetch parameters
        int fetchAmount = this.corePropertiesCIds.getMaxIdCacheLength() - this.cIdCache.size();
        int idCoreLength = getDigitCount(this.corePropertiesCIds.getNumberOfDailyIds());
        String fullPrefix = this.corePropertiesCIds.getPrefix() + this.corePropertiesCIds.getDateFormat().format(new Date());

        // Fetch free Ids
        this.cIdFetcher = new CommunityIdFetcher(fetchAmount, idCoreLength, fullPrefix, this.cIdCache);
        this.cIdFetcher.start();

        return fetchedId;
    }
    public int getFreeDId(String communityId) throws OutOfIdsException
    {
        ArrayList<Integer> dIdCache = new ArrayList<Integer>();
        DotoIdFetcher dIdFetcher = new DotoIdFetcher(communityId,
                this.corePropertiesDIds.getMaxIdCacheLength() - this.corePropertiesDIds.getMinIdCacheLength(),
                this.corePropertiesDIds.getNumberOfDailyIds(), dIdCache);

        if(dIdCache.size() == 0) {
            throw new OutOfIdsException("All Ids for your Community have been taken. Delete some older Dotos maybe!");
        }
        return dIdCache.get(0);
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