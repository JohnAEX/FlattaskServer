package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class LoginManagedBean implements Serializable{
    /*
     * Used to allow the user to login, as well as forward to the respected page for registration
     */

    //Currently used for debugging
    @PostConstruct
    public void sessionInitialized() {
        System.out.println("\t> LoginManagedBean created @" + System.currentTimeMillis());
    }

    //Currently used for debugging
    @PreDestroy
    public void sessionDestroyed() {
        System.out.println("\t> LoginManagedBean destroyed @" + System.currentTimeMillis());

    }

    public boolean loggedIn = false;

    private String username = "";
    private String password = "";
    public Participator loggedInUser = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String attemptLogin(){
        System.out.println("Attempted Login for: " + this.username + " - " + this.password); //Debugging output
        Databasehandler.instanceOf().initObjectDBConnection();
        Participator testLogin = Databasehandler.instanceOf().getParticipatorByLogin(this.username, this.password);

        try {
            if (testLogin != null) {
                //Only enters if Login was successful
                setLoggedIn(true);
                loggedInUser = testLogin;
                Databasehandler.instanceOf().close();
                System.out.println("Login successful"); //Debugging output
                if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                    //Redirects Admin users to the Sysadmin page
                    FacesContext.getCurrentInstance().getExternalContext().redirect("highlysecured/systemadmin.xhtml");
                    FacesContext.getCurrentInstance().responseComplete();
                    return "highlysecured/systemadmin";
                } else {
                    //Checks if the user is part of a community
                    if(loggedInUser.getCommunityId().length() > 2) {
                        //If yes, redirects to the main dashboard page
                        FacesContext.getCurrentInstance().getExternalContext().redirect("secured/main.xhtml");
                        FacesContext.getCurrentInstance().responseComplete();
                        return "secured/main";
                    }else{
                        //If no, redirects to the nocommunity page to either join or create one
                        FacesContext.getCurrentInstance().getExternalContext().redirect("secured/nocommunity.xhtml");
                        FacesContext.getCurrentInstance().responseComplete();
                        return "secured/nocommunity";
                    }
                }
            } else {
                //Display a failed login message and redirect to login
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
                Databasehandler.instanceOf().close();
                System.out.println("Login failed");
                return "login";
            }

        }catch (IOException e){
            e.printStackTrace();
            return "login";
        }
    }

    public String logout() {
        //Enables the option to logout
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public Participator getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Participator loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
