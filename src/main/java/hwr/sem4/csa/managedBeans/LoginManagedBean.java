package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class LoginManagedBean {

    private String username = "";
    private String password = "";

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
        System.out.println("Attempted Login for: " + this.username + " - " + this.password);
        Databasehandler.instanceOf().initObjectDBConnection();
        if(Databasehandler.instanceOf().getParticipatorByLogin(this.username, this.password) != null){
            Databasehandler.instanceOf().close();
            System.out.println("Login successful");
            return "dashboard";
        }else{
            Databasehandler.instanceOf().close();
            System.out.println("Login failed");
            return "login";

        }
    }
}
