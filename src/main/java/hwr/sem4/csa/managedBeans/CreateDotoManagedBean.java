/* Managed Bean for Create Task Page
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.comparators.DotosComparator;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean (name="CreateDotoManagedBean")
@ViewScoped

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
    private ArrayList<Participator> usersPossible = new ArrayList<>();
    private ArrayList<Task> commonTasks = new ArrayList<>();
    private Task selectedTemplate = null;
    private Databasehandler database = Databasehandler.instanceOf();
    private String selectedTaskString;
    private ArrayList<String> allTaskStrings = new ArrayList<String>();
    private String selectedUserString;
    private ArrayList<String> allUserStrings = new ArrayList<String>();


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
        this.setUsersPossible(this.searchForPossibleUsers());
        this.setCommonTasks(generateTestTasks());
        this.setValueMax(this.userAssign.getBalance());
        this.convertTaskListToStringList();
        this.convertUserListToStringList();
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
                break;
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

    public void handleChangeStringToUser(){
        System.out.println("Selected User:" + this.selectedUserString);
        if(this.selectedUserString != "") {
            boolean found = false;
            Participator fallback = new Participator();
            for(Participator p : usersPossible){
                System.out.println("Comparing: " + p.getUsername() + " & " + this.getSelectedUserString());
                if(p.getUsername().equals(selectedUserString)){
                    fallback = p;
                    found = true;
                    break;
                }
            }

            if(found){
                this.setUserAssigned(fallback);
                System.out.println("Set UserAssigned to: " + fallback.getUsername());
                System.out.println(fallback.getFirstName());
            }else{
                System.out.println("<USER NOT FOUND IN ORIGINAL LIST>");
                this.setUserAssigned(fallback);
            }

        }
        else{
            System.out.println("Assigned User not changed");
        }
    }
    //This method converts the loaded 'commonTasks' to a String arrayList based on the Titles
    private void convertTaskListToStringList(){
        for(Task t : commonTasks){
            allTaskStrings.add(t.getTitle());
        }
    }

    private void convertUserListToStringList(){
        for(Participator u : usersPossible){
            allUserStrings.add(u.getUsername());
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

    /*Grap Users in Community of assigning User, except the assigning user himself*/
    private ArrayList<Participator> searchForPossibleUsers(){
        Participator part = this.userAssign;
        ArrayList<Participator> rl = new ArrayList<>();
        if(part.getCommunityId()!=null) {
            database.initObjectDBConnection();
            List<Participator> possiblePart = database.getParticipatorsByCommunityID(part.getCommunityId());
            database.close();
            if (possiblePart.size() > 0) {
                for (int i = 0; i < possiblePart.size(); i++) {
                    System.out.println("Possible User: "+possiblePart.get(i).getUsername());
                    System.out.println("Username: " + this.userAssign.getUsername());

                    if(!possiblePart.get(i).getUsername().equals(this.userAssign.getUsername())) {
                        rl.add(possiblePart.get(i));
                        System.out.println("added: " + possiblePart.get(i));
                    }
                }
            }
        }
        return rl;
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

    /*Generate Test-Cases for Tasks, please delete or comment on final implementation*/
    /*TODO Delete or Comment*/
    private ArrayList<Task> generateTestTasks(){
        Task a1 = new Task( "Aufgabe 1", "blablablablabla", 5, 5);
        Task a2 = new Task( "Aufgabe 2", "blablablablabla", 5, 5);
        ArrayList<Task> rsf = new ArrayList<>();
        rsf.add(a1);
        rsf.add(a2);
        return rsf;

    }

    /*Change Presets of labels when Template is selected
    * Depracted: JohnFix
    * */
    /*
    public void changeTemplate(){
        /*this.setTitle(this.selectedTemplate.getTitle());
        this.setDescription(this.selectedTemplate.getDescription());
        this.setValue(this.selectedTemplate.getBaseValue());
        this.setTitle("JAJAJAJ");
        System.out.println(this.getTitle());
        System.out.println("Changed Template");
    }
*/

    /*Store Task in Database, assign to User*/
    public void confirmDoto(){

        /*Debug*/
        System.out.println("CreateDoto:");
        System.out.println(this.getTitle());
        System.out.println(this.getDescription());
        System.out.println(this.getValue());
        System.out.println(this.getDuration());
        System.out.println(this.getUserAssigned().getUsername());
        /*End of debug*/


        Dotos d = new Dotos(this.title, this.description, this.value, this.duration, this.userAssigned.getUsername(), this.userAssign.getUsername());
        String CID = this.userAssign.getCommunityId();
        System.out.println(CID);
        database.initObjectDBConnection();
        Community com = database.getCommunityById(CID);
        d.setId(this.getFreeDId(com));
        ArrayList<Dotos> oldDotos = com.getDotosList();
        oldDotos.add(d);
        database.updateCommunity(com.getId(), com.getName(), com.getCreationTime(), com.getTaskList(), oldDotos);
        userAssign.setBalance(userAssign.getBalance()-(this.getValue()));
        database.updateParticipator(userAssign.getUsername(),userAssign.getPassword(),
                userAssign.getFirstName(),userAssign.getLastName(),userAssign.getBalance(),
                userAssign.getRole(),userAssign.getCommunityId(),userAssign.getCreationTime());
        database.close();
        System.out.println("-----------------------------------");
        refresh();
    }

    /*
    * Inserted by Dominik for proper IdHandling
     */

    private int getFreeDId(Community containingCom)
    {
        // Necessary sort to determine free Ids in a structured manner
        containingCom.getDotosList().sort(new DotosComparator());

        int lastDotoId = 0;
        for(Iterator<Dotos> dotoListIt = containingCom.getDotosList().iterator(); dotoListIt.hasNext(); ) {
            // Iterate over all present Dotos
            Dotos currentDoto = dotoListIt.next();

            // Determine delta of last and current DotoId
            int idDelta = currentDoto.getId() - lastDotoId;
            if(idDelta >= 2) {
                // Delta >= 2 i.e. at least one Id in between is free
                return lastDotoId + 1;
            }
            // Update lastId
            lastDotoId = currentDoto.getId();
        }

        // If no free Ids in between the Id following the highest Id is returned
        return containingCom.getDotosList().get(containingCom.getDotosList().size() - 1).getId() + 1;
    }

    /*
    * Stolen from Johns Code
    * */

    public void refresh(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /***************************************
     * Getter & Setter
     * ***************************************/

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

    public String getSelectedUserString() {
        return selectedUserString;
    }

    public void setSelectedUserString(String selectedUserString) {
        this.selectedUserString = selectedUserString;
    }

    public ArrayList<String> getAllUserStrings() {
        return allUserStrings;
    }

    public void setAllUserStrings(ArrayList<String> allUserStrings) {
        this.allUserStrings = allUserStrings;
    }
}
