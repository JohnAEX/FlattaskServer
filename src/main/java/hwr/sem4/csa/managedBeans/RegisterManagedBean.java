package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.LocalDateTime;

@ManagedBean
public class RegisterManagedBean {

    private String username;
    private String password;
    private String repassword;
    private String firstName = "";
    private String lastName = "";
    private int balance;
    private String role;
    private String history;
    private String creationTime;

    //Functional Methods

    public void attemptRegistration() {
        Databasehandler.instanceOf().initObjectDBConnection();
        if(Databasehandler.instanceOf().getParticipatorByUsername(this.username) == null){
            Participator p = new Participator();
            p.setUsername(this.username);
            p.setPassword(this.password);
            p.setFirstName(this.firstName);
            p.setLastName(this.lastName);
            p.setRole("user");
            p.setHistory("");
            p.setCommunityId("");
            p.setCreationTime(LocalDateTime.now().toString());
            p.setBalance(100);


            Databasehandler.instanceOf().insert(p);
            if (Databasehandler.instanceOf().getParticipatorByLogin(this.username, this.password) != null) {
                Databasehandler.instanceOf().close();
             try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
             } catch (IOException e) {
                e.printStackTrace();
                }
            } else {
                /*Databasehandler.instanceOf().close();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("register.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
        }

    }
    }



    //Getter and Setter

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

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
