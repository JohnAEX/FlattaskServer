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
    /*
     * Used to join communities as new Players, required: CID, to prevent random joining
     */
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
        loggedInUser = login.getLoggedInUser(); //Get logged in User
        Databasehandler.instanceOf().initObjectDBConnection();
        if(Databasehandler.instanceOf().getCommunityById(cid) != null){
            //Only enters if community exists
            loggedInUser.setCommunityId(cid);
            Databasehandler.instanceOf().updateParticipator(loggedInUser.getUsername(),loggedInUser.getPassword(),
                    loggedInUser.getFirstName(),loggedInUser.getLastName(),loggedInUser.getBalance(),
                    loggedInUser.getRole(),loggedInUser.getCommunityId(),loggedInUser.getCreationTime());
            Databasehandler.instanceOf().close();
            try {
                //Try to redirect to the main dashboard page
                FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            errorMessage = "No community with that ID exists.";
            //Error message in case that the community doesn't exist
        }

        Databasehandler.instanceOf().close();

        return "joinCommunity";
    }


}
