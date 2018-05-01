/* Managed Bean for Create Task Page
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@ManagedBean (name="ProfileManagedBean")
@SessionScoped

public class ProfileManagedBean {
    /*******************
    * Attributes
    * *******************/
    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String fullName = "";
    private int balance = 0;
    private String comname = "";
    private Participator user = null;


    /*****************
    * Constructor
    ***************** */
    public ProfileManagedBean(){
        this.setUser(this.userInit());
        this.setUsername(this.user.getUsername());
        this.setFirstName(this.user.getFirstName());
        this.setLastName(this.user.getLastName());
        this.setBalance(this.user.getBalance());
        this.setComname(this.grapCommunityName());
        this.setFullName(this.getFirstName() + " " + this.getLastName());
    }

    /***********************************
     * Functional Methods
     * **********************************/

    /*Grap Username from Session*/

    private Participator userInit(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        System.out.println("Assigning User: "+login.loggedInUser.getUsername());
        Participator rs = login.loggedInUser;
        return rs;
    }

    private String grapCommunityName(){
        Databasehandler db = Databasehandler.instanceOf();
        db.initObjectDBConnection();
        if(this.user.getCommunityId()!=null){
            Community com = db.getCommunityById(this.user.getCommunityId());
            db.close();
            return com.getName();
        }
        else{
            db.close();
            return "Currently no Community";
        }
    }



    /***************************************
     * Getter & Setter
     * ***************************************/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public Participator getUser() {
        return user;
    }

    public void setUser(Participator user) {
        this.user = user;
    }
}
