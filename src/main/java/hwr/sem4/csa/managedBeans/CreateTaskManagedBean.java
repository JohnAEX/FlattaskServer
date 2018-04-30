/* Managed Bean for Create Task Page
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.List;
import java.util.ArrayList;

@ManagedBean (name="CreateTaskManagedBean")
@SessionScoped

public class CreateTaskManagedBean {
    /*
    * Attributes
    * */
    private String title = "";
    private String description = "";
    private Participator userAssign = null;
    private Participator userAssigned = null;
    private int value = 0;
    private int duration = 0;
    private int valueMax = 5;
    private ArrayList<Participator> usersPossible = null;
    private ArrayList<Task> commonTasks = null;
    private Task selectedTemplate = null;
    private Databasehandler database = Databasehandler.instanceOf();


    /*
    * Constructor
    * */
    public CreateTaskManagedBean(){
       // this.setUsersPossible(searchForPossibleUsers());
        this.setUsersPossible(generateTestUsers());
        this.setCommonTasks(generateTestTasks());
    }

    /*Functional Methods*/

  /*  private Participator grapSession(){

    }*/

    private ArrayList<Participator> searchForPossibleUsers(){
        Participator part = database.getParticipatorByUsername(this.userAssign.getUsername());
        List<Participator> possiblePart = database.getParticipatorsByCommunityID(part.getCommunityId());
        ArrayList<Participator> rl = new ArrayList<>();
        if(possiblePart.size() > 0) {
            for (int i = 0; i < possiblePart.size(); i++) {
                rl.add(possiblePart.get(i));
            }
        }
        return rl;
    }

    private  ArrayList<Participator> generateTestUsers(){
        Participator a = new Participator();
        a.setFirstName("Hans");
        a.setLastName("Peter");
        Participator b = new Participator();
        b.setFirstName("Lara");
        b. setLastName("Peters");
        ArrayList<Participator> rs = new ArrayList<>();
        rs.add(a);
        rs.add(b);
        rs.add(a);
        return rs;

    }

  /*  private ArrayList<Task> generateCommonTasks(){

    }*/

    private ArrayList<Task> generateTestTasks(){
        Task a1 = new Task("1", "Aufgabe 1", "blablablablabla", 5, 5);
        Task a2 = new Task("1", "Aufgabe 2", "blablablablabla", 5, 5);
        ArrayList<Task> rsf = new ArrayList<>();
        rsf.add(a1);
        rsf.add(a2);
        return rsf;

    }

    public void changeTemplate(AjaxBehaviorEvent event){
        /*this.setTitle(this.selectedTemplate.getTitle());
        this.setDescription(this.selectedTemplate.getDescription());
        this.setValue(this.selectedTemplate.getBaseValue());*/
        title = "JAJAJAJ";
        System.out.println("Changed Template");
    }

    public void confirmTask(){

    }

    /*Getter & Setter*/

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

    public Participator getUserAssign() {
        return userAssign;
    }

    public void setUserAssign(Participator userAssign) {
        this.userAssign = userAssign;
    }

    public Participator getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(Participator userAssigned) {
        this.userAssigned = userAssigned;
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

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }

    public ArrayList<Participator> getUsersPossible() {
        return usersPossible;
    }

    public void setUsersPossible(ArrayList<Participator> usersPossible) {
        this.usersPossible = usersPossible;
    }

    public ArrayList<Task> getCommonTasks() {
        return commonTasks;
    }

    public void setCommonTasks(ArrayList<Task> commonTasks) {
        this.commonTasks = commonTasks;
    }

    public Task getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(Task selectedTemplate) {
        System.out.println("Set Task:" + selectedTemplate.getTitle());
        this.selectedTemplate = selectedTemplate;
       // this.changeTemplate();
    }
}
