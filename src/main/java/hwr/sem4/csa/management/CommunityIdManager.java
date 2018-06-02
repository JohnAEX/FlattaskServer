package hwr.sem4.csa.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwr.sem4.csa.exceptions.OutOfIdsException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static hwr.sem4.csa.management.CommunityIdMgtConstants.COMMUNITY_MGT_DEFAULTS_FILE;

public class CommunityIdManager extends IdManagementCore {
    private static CommunityIdManager thisCommunityIdManager = new CommunityIdManager();

    private IdManagementCore coreProperties;

    private String lowerCoreBoundary;
    private String upperCoreBoundary;
    private boolean idDepletion;
    private ArrayList<String> idCache;

    private IdFetcher idFetcher;

    public static CommunityIdManager getInstance()
    {
        return thisCommunityIdManager;
    }

    private CommunityIdManager()
    {
        // Load default configs
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inFromFile = loader.getResourceAsStream(COMMUNITY_MGT_DEFAULTS_FILE);
        try {
            this.coreProperties = new ObjectMapper().readValue(inFromFile, IdManagementCore.class);
        } catch(IOException ioExc) {
            // Log this
            throw new IllegalArgumentException("Could not read config file \"" + COMMUNITY_MGT_DEFAULTS_FILE + "\": " + ioExc.getMessage());
        }

        this.lowerCoreBoundary = StringUtils.leftPad("", this.coreProperties.getIdCoreLength(), '0');
        this.upperCoreBoundary = StringUtils.leftPad("", this.coreProperties.getIdCoreLength(), 'F');
        this.idCache = new ArrayList<String>();

        //Initial search for free Ids
        this.idFetcher = new IdFetcher(this.coreProperties.getPrefix() + "-" + this.coreProperties.getDateFormat().format(new Date()) + "-", this.coreProperties.getIdCoreLength(),
                this.lowerCoreBoundary, this.upperCoreBoundary, this.coreProperties.getMaxIdCacheLength(), this.idCache, this.idDepletion);
        this.idFetcher.start();
        try {
            this.idFetcher.join();
        }catch (InterruptedException iExc) {
            // Log this
        }
    }

    public String getNewId() throws OutOfIdsException
    {
        // Wait for IdFetcher to finish if Cache has trespassed lower limit
        if(this.idCache.size() < this.coreProperties.getMinIdCacheLength()) {
            try{
                idFetcher.join();
            } catch(InterruptedException iExc) {
                // Log this
            }
        }

        // Break if no Ids are available
        if(idDepletion) {
            throw new OutOfIdsException("All Ids have been consumed for the date " + this.coreProperties.getDateFormat().format(new Date()) + ". Try again later.");
        }

        // Grab unused Id
        String freeId = this.idCache.get(0);
        this.idCache.remove(0);

        // Fetch Ids if Cache has trespassed lower limit
        if(this.idCache.size() < this.coreProperties.getMinIdCacheLength()) {
            this.idFetcher = new IdFetcher(this.coreProperties.getPrefix() + "-" + this.coreProperties.getDateFormat().format(new Date()) + "-", this.coreProperties.getIdCoreLength(),
                    this.lowerCoreBoundary, this.upperCoreBoundary, this.coreProperties.getMaxIdCacheLength(), this.idCache, this.idDepletion);
            this.idFetcher.start();
        }

        return freeId;
    }

}