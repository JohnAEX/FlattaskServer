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

    public Community(){}

    public Community(String id, String name, String creationTime, ArrayList<Task> taskList, ArrayList<Dotos> dotosList) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.taskList = taskList;
        this.dotosList = dotosList;
    }

    public Community(String id, String name, String creationTime) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        ArrayList<Dotos> dotos = new ArrayList<Dotos>();
        Dotos d = new Dotos("abc", "dfg",5,5,"Lucas","Lucas2");
        dotos.add(d);
        this.dotosList = dotos;
        this.taskList = new ArrayList<Task>();
    }

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
