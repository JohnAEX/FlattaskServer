package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class LoginManagedBean {

    private String username = "";
    private String password = "";

    public String getUsername() {
        System.out.println("\t>Something happened");
        return username;
    }

    public void setUsername(String username) {
        System.out.println("\t>Something happened2");
        this.username = username;
    }

    public String getPassword() {
        System.out.println("\t>Something happened");
        return password;
    }

    public void setPassword(String password) {
        System.out.println("\t>Something happened2");
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
