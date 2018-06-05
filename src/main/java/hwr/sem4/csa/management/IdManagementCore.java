package hwr.sem4.csa.management;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class IdManagementCore {

    private DateFormat dateFormat;
    private String prefix;
    private int numberOfDailyIds;
    private int minIdCacheLength;
    private int maxIdCacheLength;

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getNumberOfDailyIds() {
        return numberOfDailyIds;
    }

    public void setNumberOfDailyIds(int numberOfDailyIds) {
        this.numberOfDailyIds = numberOfDailyIds;
    }

    public int getMinIdCacheLength() {
        return minIdCacheLength;
    }

    public void setMinIdCacheLength(int minIdCacheLength) {
        this.minIdCacheLength = minIdCacheLength;
    }

    public int getMaxIdCacheLength() {
        return maxIdCacheLength;
    }

    public void setMaxIdCacheLength(int maxIdCacheLength) {
        this.maxIdCacheLength = maxIdCacheLength;
    }

}