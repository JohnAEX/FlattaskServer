package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean
@ViewScoped
public class ProfileSettingsManagedBean {

    private String username = "";
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private int balance = 0;
    private String creationTime = "";
    private Participator user = null;
    private String communityID = "";
    private String role = "";
    private String errorMsg = "";
    private String oldPsw = "";


    @PostConstruct
    public void init(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        user = login.loggedInUser;
        username = user.getUsername();
        oldPsw = user.getPassword();
        int passwordLength = user.getPassword().length();
        while(passwordLength>0){
            password += '*';
            passwordLength--;
        }
        firstName = user.getFirstName();
        lastName = user.getLastName();
        balance = user.getBalance();
        creationTime = user.getCreationTime();
        communityID = user.getCommunityId();
        role = user.getRole();

    }

    public void submit(){
        //System.out.println("PSW: " + password + "\nFN: " + firstName + "\nLN: " + lastName);
        if(password.length()>2 && firstName.length() > 2 && lastName.length() > 2){
            if(password.contains("*")){
                password = oldPsw;
            }
            Databasehandler.instanceOf().initObjectDBConnection();
            Databasehandler.instanceOf().updateParticipator(username, password, firstName,lastName,balance,role,communityID,creationTime);
            Databasehandler.instanceOf().close();
        }else{
            errorMsg = "Something went wrong, please retry at a later point of time.";
        }
        refresh();

    }
    //Method used to force a refresh once called
    public void refresh(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

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

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public Participator getUser() {
        return user;
    }

    public void setUser(Participator user) {
        this.user = user;
    }
}
