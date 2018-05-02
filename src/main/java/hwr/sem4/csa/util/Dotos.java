package hwr.sem4.csa.util;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;



@Embeddable
/*@Entity
@Table (name="Dotos")*/
public class Dotos implements Serializable{

    static final long serialVersionUID = 42L;
    private int id = 0;
    private String title = "";
    private String description ="";
    private int value = 0;
    private int duration = 0;
    private String assignedTo = null;
    private String assignedBy = null;
   /* private String communityID = "";*/

    public Dotos(){

    }

    public Dotos(String title, String description, int value, int duration, String assignedTo, String assignedBy) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.duration = duration;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
    }

  /* public Dotos(String title, String description, int value, int duration, Participator assignedTo, Participator assignedBy, String communityID) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.duration = duration;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.communityID = communityID;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    /*

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    */
}
