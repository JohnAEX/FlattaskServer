package hwr.sem4.csa.util;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "Communities")
public class Community implements Serializable {

    @Id
    private String id;
    private String name;
    private String creationTime;
    @OneToOne(cascade = CascadeType.ALL)
    private ArrayList<Task> taskList = new ArrayList<Task>();
    @OneToOne(cascade = CascadeType.ALL)
    private ArrayList<Dotos> dotosList = new ArrayList<Dotos>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public ArrayList<Dotos> getDotosList() {
        return dotosList;
    }

    public void setDotosList(ArrayList<Dotos> dotosList) {
        this.dotosList = dotosList;
    }
}
