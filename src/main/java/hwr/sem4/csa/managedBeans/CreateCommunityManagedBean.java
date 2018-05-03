package hwr.sem4.csa.managedBeans;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

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

    private String cid;
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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
        loggedInUser = login.getLoggedInUser();
        Databasehandler.instanceOf().initObjectDBConnection();
        if (Databasehandler.instanceOf().getCommunityById(cid) == null) {
            Community c = new Community();
            c.setId(cid);
            c.setName(cname);
            c.setCreationTime(LocalDateTime.now().toString());
            c.setDotosList(new ArrayList<Dotos>());
            c.setTaskList(new ArrayList<Task>());
            Databasehandler.instanceOf().insert(c);
            //set Participator CID
            loggedInUser.setCommunityId(cid);
            Databasehandler.instanceOf().updateParticipator(loggedInUser.getUsername(), loggedInUser.getPassword(),
                    loggedInUser.getFirstName(), loggedInUser.getLastName(), loggedInUser.getBalance(),
                    loggedInUser.getRole(), loggedInUser.getCommunityId(), loggedInUser.getCreationTime());
            Databasehandler.instanceOf().close();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMessage = "A community with that ID already exists, please choose another one.";
        }
        Databasehandler.instanceOf().close();
        return "createCommunity";
    }
}
