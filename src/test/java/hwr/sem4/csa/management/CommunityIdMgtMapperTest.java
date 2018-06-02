package hwr.sem4.csa.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static hwr.sem4.csa.management.CommunityIdMgtConstants.COMMUNITY_MGT_DEFAULTS_FILE;

public class CommunityIdMgtMapperTest {
    private InputStream singleInFromFile;
    private InputStream multipleInFromFile;

    @Test
    public void fileCheck()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        this.singleInFromFile = loader.getResourceAsStream(COMMUNITY_MGT_DEFAULTS_FILE);

        if(this.singleInFromFile == null) {
            throw new IllegalArgumentException("IDManagement-config file \"" + COMMUNITY_MGT_DEFAULTS_FILE + "\" could not be read.");
        }

        String arrayContainingFilePath = "DefaultJsonCores.json";
        this.multipleInFromFile = loader.getResourceAsStream(arrayContainingFilePath);

        if(this.multipleInFromFile == null) {
            throw new IllegalArgumentException("IDManagement-config file \"" + arrayContainingFilePath + "\" could not be read.");
        }
    }

    @Test
    public void mapperCheck()
    {
        try {
            IdManagementCore testCore = new ObjectMapper().readValue(this.singleInFromFile, IdManagementCore.class);
        } catch(IOException ioExc) {
            throw new IllegalArgumentException("IdManagement-config file \"" + COMMUNITY_MGT_DEFAULTS_FILE + "\" " +
                    "could not be mapped to an IDManagementCore object.");
        }

        /*
        try {
            Gson googleJson = new Gson();

            ArrayList<IdManagementCore> testCores = new ArrayList<>();
            testCores = googleJson.fromJson(multipleInFromFile, testCores.getClass());
        }
        */
    }
}
