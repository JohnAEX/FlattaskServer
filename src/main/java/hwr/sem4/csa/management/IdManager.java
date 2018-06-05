package hwr.sem4.csa.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwr.sem4.csa.exceptions.OutOfIdsException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static hwr.sem4.csa.management.IdManagementConstants.COMMUNITY_MGT_DEFAULTS_FILE;

public class IdManager extends IdManagementCore {
    private static IdManager thisIdManager = new IdManager();

    private IdManagementCore coreProperties;

    private String lowerCoreBoundary;
    private String upperCoreBoundary;
    private boolean idDepletion;
    private ArrayList<String> idCache;

    private CommunityIdFetcher communityIdFetcher;

    public static IdManager getInstance()
    {
        return thisIdManager;
    }

    private IdManager()
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
        this.communityIdFetcher = new CommunityIdFetcher(this.coreProperties.getPrefix() + "-" + this.coreProperties.getDateFormat().format(new Date()) + "-", this.coreProperties.getIdCoreLength(),
                this.lowerCoreBoundary, this.upperCoreBoundary, this.coreProperties.getMaxIdCacheLength(), this.idCache, this.idDepletion);
        this.communityIdFetcher.start();
        try {
            this.communityIdFetcher.join();
        }catch (InterruptedException iExc) {
            // Log this
        }
    }

    public String getNewId() throws OutOfIdsException
    {
        return CommunityIdFetcher.smartFetch(this.communityIdFetcher, this.coreProperties, this.lowerCoreBoundary, this.upperCoreBoundary, this.idCache, this.idDepletion);
    }

}