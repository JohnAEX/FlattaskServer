package hwr.sem4.csa.management;

import hwr.sem4.csa.database.Databasehandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class IdManagementCore {

    private DateFormat dateFormat;
    private String prefix;
    private int idCoreLength;
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

    public int getIdCoreLength() {
        return idCoreLength;
    }

    public void setIdCoreLength(int idCoreLength) {
        this.idCoreLength = idCoreLength;
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