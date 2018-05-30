package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityIdManager {

    private static Databasehandler localHandler = Databasehandler.instanceOf();
    private static char[] p1atoms = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    private static int[] p2atoms = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    };
    private static int idLength = 6;
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
            String idCheck = "000000";

            while(nextFreeIds.size() < maxIdCacheLength){

            }
        }

        String nextFreeId = nextFreeIds.get(0);
        nextFreeIds.remove(0);
        return "C-" + nextFreeId;
    }


}
