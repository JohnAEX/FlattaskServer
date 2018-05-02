package hwr.sem4.csa.util;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Embeddable
public class Dotos implements Serializable{

    public ArrayList<String> participatorlist = new ArrayList<>();

    public String title;

    //Last Date or just Days ? in der Get Methode die Duration ausrechnen von jetzt zu dem Datum
    public Date duration;

    public int value;

    public String notes;

    public ArrayList<String> getParticipatorlist() {
        return participatorlist;
    }

    public void setParticipatorlist(ArrayList<String> participatorlist) {
        this.participatorlist = participatorlist;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}