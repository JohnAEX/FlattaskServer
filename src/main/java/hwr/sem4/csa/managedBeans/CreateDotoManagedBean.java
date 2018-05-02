/* Managed Bean for Create Task Page
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@ManagedBean (name="CreateDotoManagedBean")
@RequestScoped

public class CreateDotoManagedBean {
    /*******************
    * Attributes
    * *******************/
    private String title = "";
    private String description = "";
    private Participator userAssign = null;
    private Participator userAssigned = null;
    private int value = 0;
    private int duration = 0;
    private int valueMax = 0;
    private ArrayList<Participator> usersPossible = null;
    private ArrayList<Task> commonTasks = null;
    private Task selectedTemplate = null;
    private Databasehandler database = Databasehandler.instanceOf();
    private String selectedTaskString;
    private ArrayList<String> allTaskStrings = new ArrayList<String>();


    /*****************
    * Constructor -> Invalid, Usage of Constructors is not recommended, rather use the @PostConstruct init Method
    ***************** */
    public CreateDotoManagedBean(){
       // this.setUsersPossible(searchForPossibleUsers());

    }

    //Method gets called after JSF has finished creating the bean, is safer than using a constructor
    @PostConstruct
    public void init(){
        System.out.println("CDMB-Number: ");
        this.setUserAssign(this.userInit());
        this.setUsersPossible(this.generateTestUsers());
        this.setUsersPossible(this.searchForPossibleUsers());
        this.setCommonTasks(generateTestTasks());
        this.setValueMax(this.userAssign.getBalance());
        this.convertTaskListToStringList();
    }

    /***********************************
     * Functional Methods
     * **********************************/

//This method acts as a new ChangeHandler, rebuilding the Task for display, if it was found in the List
    public void handleChangeStringToTask(){
        Task currentTask = new Task("notSet","notSet",0,0);
        boolean found = false;
        for(Task t : commonTasks){
            if(t.getTitle().equals(selectedTaskString)){
                currentTask = t;
                found = true;
            }
        }
        if(found) {
            this.setTitle(currentTask.getTitle());
            this.setDescription(currentTask.getDescription());
            this.setValue(currentTask.getBaseValue());
            this.setDuration(currentTask.getBaseDuration());
        }else{
            //Error handling request
            this.setTitle("ERROR");
            this.setDescription("Input invalid");
            this.setValue(0);
            this.setDuration(404);
        }
    }
    //This method converts the loaded 'commonTasks' to a String arrayList based on the Titles
    private void convertTaskListToStringList(){
        for(Task t : commonTasks){
            allTaskStrings.add(t.getTitle());
        }
    }

    /*Grap Username from Session*/

    private Participator userInit(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        System.out.println("Assigning User: "+login.loggedInUser.getUsername());
        Participator rs = login.loggedInUser;
        return rs;
    }

    /*Grap Users in Community of assigning User*/
    private ArrayList<Participator> searchForPossibleUsers(){
        Participator part = this.userAssign;
        ArrayList<Participator> rl = new ArrayList<>();
        if(part.getCommunityId()!=null) {
            database.initObjectDBConnection();
            List<Participator> possiblePart = database.getParticipatorsByCommunityID(part.getCommunityId());
            database.close();
            if (possiblePart.size() > 0) {
                for (int i = 0; i < possiblePart.size(); i++) {
                    rl.add(possiblePart.get(i));
                }
            }
        }
        return rl;
    }

    /*Generate Test Users - Wrote while DB-Handler class was incomplete*/
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

    /*Grab Task-List from Community of assigning User*/
    private ArrayList<Task> generateCommonTasks(){
        database.initObjectDBConnection();
        Community com = database.getCommunityById(this.userAssign.getCommunityId());
        database.close();
        if(com.getTaskList()!=null) {
            return com.getTaskList();
        }
        else{
            ArrayList<Task> noTasks = new ArrayList<Task>();
            noTasks.add(new Task("Create Tasks!","",0,0));
            return noTasks;
        }
    }

    /*Generate Test-Cases for Tasks*/
    private ArrayList<Task> generateTestTasks(){
        Task a1 = new Task( "Aufgabe 1", "blablablablabla", 5, 5);
        Task a2 = new Task( "Aufgabe 2", "blablablablabla", 5, 5);
        ArrayList<Task> rsf = new ArrayList<>();
        rsf.add(a1);
        rsf.add(a2);
        return rsf;

    }

    /*Change Presets of labels when Template is selected*/
    public void changeTemplate(){
        /*this.setTitle(this.selectedTemplate.getTitle());
        this.setDescription(this.selectedTemplate.getDescription());
        this.setValue(this.selectedTemplate.getBaseValue());*/
        this.setTitle("JAJAJAJ");
        System.out.println(this.getTitle());
        System.out.println("Changed Template");
    }

    /*Store Task in Database, assign to User*/
    public void confirmDoto(){
        String CID = this.userAssign.getCommunityId();
        Dotos d = new Dotos(this.title, this.description, this.value, this.duration, this.userAssigned, this.userAssign);
        database.initObjectDBConnection();
        Community com = database.getCommunityById(CID);
        ArrayList<Dotos> oldDotos= com.getDotosList();
        oldDotos.add(d);
        database.updateCommunity(com.getId(), com.getName(), com.getCreationTime());
        database.close();

    }

    public String getSelectedTaskString() {
        return selectedTaskString;
    }

    public void setSelectedTaskString(String selectedTaskString) {
        this.selectedTaskString = selectedTaskString;
    }

    public ArrayList<String> getAllTaskStrings() {
        return allTaskStrings;
    }

    public void setAllTaskStrings(ArrayList<String> allTaskStrings) {
        this.allTaskStrings = allTaskStrings;
    }

    /***************************************
     * Getter & Setter
     * ***************************************/



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
