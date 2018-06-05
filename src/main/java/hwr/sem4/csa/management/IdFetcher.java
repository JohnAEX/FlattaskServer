package hwr.sem4.csa.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class IdFetcher extends Thread {

    private IdManagementCore coreProperties;
    private String lowerCoreBoundary;
    private String upperCoreBoundary;
    private ArrayList<String> idStore;
    private boolean idDepletion;

    protected IdFetcher()
    {
        this.coreProperties = null;
        this.lowerCoreBoundary = null;
        this.upperCoreBoundary = null;
        this.idStore = new ArrayList<String>();
        this.idDepletion = false;
    }

    protected boolean loadCorePropertiesFromFile(String filePath)
    {
        // Load default configs
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inFromFile = loader.getResourceAsStream(filePath);
        try {
            this.coreProperties = new ObjectMapper().readValue(inFromFile, IdManagementCore.class);
        } catch(IOException ioExc) {
            return false;
        }

        //Invalid properties
        if(this.coreProperties.getPrefix() == null || this.coreProperties.getPrefix().equals("")) {
            this.coreProperties = null;
            return false;
        }
        if(this.coreProperties.getDateFormat() == null || this.coreProperties.getDateFormat().toString().equals("")) {
            this.coreProperties = null;
            return false;
        }
        if(this.coreProperties.getIdCoreLength() < 0) {
            this.coreProperties = null;
            return false;
        }
        if(this.coreProperties.getMinIdCacheLength() < 0 || this.coreProperties.getMaxIdCacheLength() < this.coreProperties.getMinIdCacheLength()) {
            this.coreProperties = null;
            return false;
        }

        // No invalid properties
        return true;
    }

    protected boolean initFieldsFromCoreProperties()
    {
        // faulty coreProperties
        if(this.coreProperties == null) {
            return false;
        }

        this.lowerCoreBoundary = StringUtils.leftPad("", this.coreProperties.getIdCoreLength(), '0');
        this.upperCoreBoundary = StringUtils.leftPad("", this.coreProperties.getIdCoreLength(), 'F');
        return true;
    }

}
