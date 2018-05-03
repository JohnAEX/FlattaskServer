package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class JoinCommunityManagedBean {
    private String cid;
    private String errorMessage = "";
    Participator loggedInUser;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String tryToJoin(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        loggedInUser = login.getLoggedInUser();
        Databasehandler.instanceOf().initObjectDBConnection();
        if(Databasehandler.instanceOf().getCommunityById(cid) != null){
            loggedInUser.setCommunityId(cid);
            Databasehandler.instanceOf().updateParticipator(loggedInUser.getUsername(),loggedInUser.getPassword(),
                    loggedInUser.getFirstName(),loggedInUser.getLastName(),loggedInUser.getBalance(),
                    loggedInUser.getRole(),loggedInUser.getCommunityId(),loggedInUser.getCreationTime());
            Databasehandler.instanceOf().close();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            errorMessage = "No community with that ID exists.";
        }

        Databasehandler.instanceOf().close();

        return "joinCommunity";
    }


}
