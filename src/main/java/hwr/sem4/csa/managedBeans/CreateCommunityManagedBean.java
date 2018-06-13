package hwr.sem4.csa.managedBeans;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.management.IdManager;
import hwr.sem4.csa.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
public class CreateCommunityManagedBean {
    /*
     * Used to create Communities by new Users
     */

    private IdManager localManager = IdManager.getInstance();
    private String cname;
    private String creationTime;
    private String errorMessage = "";
    Participator loggedInUser;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    //Functional Methods
    public String submit() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        loggedInUser = login.getLoggedInUser(); //Get the logged in User

        String cId = this.localManager.getFreeCId();
        Community c = new Community();
        c.setId(cId);
        c.setName(cname);
        c.setCreationTime(LocalDateTime.now().toString());
        c.setDotosList(new ArrayList<Dotos>());
        c.setTaskList(new DummyTaskGenerator().getDummyTasks());

        Databasehandler.instanceOf().initObjectDBConnection(); //Init DB connection
        Databasehandler.instanceOf().insert(c);
        //set Participator CID
        loggedInUser.setCommunityId(cId);
        Databasehandler.instanceOf().updateParticipator(loggedInUser.getUsername(), loggedInUser.getPassword(),
                loggedInUser.getFirstName(), loggedInUser.getLastName(), loggedInUser.getBalance(),
                loggedInUser.getRole(), loggedInUser.getCommunityId(), loggedInUser.getCreationTime());
        Databasehandler.instanceOf().close();
        try {
            //Try to redirect to the main dashboard page
            FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Databasehandler.instanceOf().close();
        return "createCommunity";
    }
}
