package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityIdManager {

    private static Databasehandler localHandler = Databasehandler.instanceOf();
    private static int idCoreLength = 10;
    private static int maxIdCacheLength = 25;
    private static int minIdCacheLength = 10;
    private static ArrayList<String> nextFreeIds;

    public static int getMaxIdCacheLength()
    {
        return maxIdCacheLength;
    }
    public static void setMaxIdCacheLength(int newIdCacheLength)
    {
        maxIdCacheLength = newIdCacheLength;
    }

    public static int getMinIdCacheLength() {
        return minIdCacheLength;
    }

    public static void setMinIdCacheLength(int newMinIdCacheLength) {
        minIdCacheLength = newMinIdCacheLength;
    }

    public static String getNextFreeId()
    {
        if(nextFreeIds.size() == minIdCacheLength){
            int[] lastFreeId = new int[idCoreLength];
            while(nextFreeIds.size() < maxIdCacheLength){

            }
        }

        String nextFreeId = nextFreeIds.get(0);
        nextFreeIds.remove(0);
        return "C-" + nextFreeId;
    }


}
