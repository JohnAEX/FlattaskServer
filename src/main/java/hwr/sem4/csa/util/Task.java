package hwr.sem4.csa.util;

import javax.persistence.Embeddable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Embeddable
/*@Entity*/
/*@Table(name="Dotos")*/
public class Task implements Serializable {
    private String id;
    private String title;
    private String description;
    private int baseValue;
    private int baseDuration;
    /*private String ccommunityID;*/

    public Task(String title, String description, int baseValue, int baseDuration) {
        this.title = title;
        this.description = description;
        this.baseValue = baseValue;
        this.baseDuration = baseDuration;
    }

   /* public Task(String title, String description, int baseValue, int baseDuration, String ccommunityID) {
        this.title = title;
        this.description = description;
        this.baseValue = baseValue;
        this.baseDuration = baseDuration;
        this.ccommunityID = ccommunityID;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public int getBaseDuration() {
        return baseDuration;
    }

    public void setBaseDuration(int baseDuration) {
        this.baseDuration = baseDuration;
    }

}
