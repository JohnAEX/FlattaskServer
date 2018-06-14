package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.comparators.ParticipatorFlattyComparator;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@ManagedBean
@ViewScoped
public class MyCommunityManagedBean {

    private Databasehandler localHandler = Databasehandler.instanceOf();

    private Participator localParticipator;
    private Community localCommunity;

    // Properties exclusively for Primeface elements
    private List<Participator> participatorList;

    private Participator selectedParticipator;
    private FacesMessage message;

    @PostConstruct
    public void init()
    {
        this.message = new FacesMessage();

        // Grab Participator object
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        this.localParticipator = login.getLoggedInUser();

        this.localHandler.initObjectDBConnection();

        // Fetch Participator List
        Optional<Community> optional = Optional.of(localHandler.getCommunityById(localParticipator.getCommunityId()));
        if(optional.isPresent()) {
            this.localCommunity = optional.get();
            this.participatorList = this.localHandler.getParticipatorsByCommunityID(this.localCommunity.getId());
            this.participatorList.sort(new ParticipatorFlattyComparator());
        } else {
            this.localCommunity = null;
            this.participatorList = null;
            this.message.setSummary("Error");
            this.message.setDetail("Your Community could not be found.");
            this.message.setSeverity(FacesMessage.SEVERITY_ERROR);
            this.displayMessage();
        }
    }

    public List<Participator> getParticipatorList()
    {
        return this.participatorList;
    }

    public Participator getSelectedParticipator() {
        return selectedParticipator;
    }
    public void setSelectedParticipator(Participator selectedParticipator) {
        this.selectedParticipator = selectedParticipator;
    }

    public Community getLocalCommunity() {
        return localCommunity;
    }

    public int getNumberOfTasks()
    {
        return this.localCommunity.getTaskList().size();
    }
    public int getNumberOfDotos()
    {
        return this.localCommunity.getDotosList().size();
    }
    public int getNumberOfOpenDotos()
    {
        int count = 0;
        // Iterate over all Dotos
        for(Iterator<Dotos> dotoIt = this.localCommunity.getDotosList().iterator(); dotoIt.hasNext(); ) {
            Dotos tempDoto = dotoIt.next();
            if(tempDoto.getAssignedTo() == null || tempDoto.equals("")) {
                // Increment count if noone assigned
                ++count;
            }
        }
        return count;
    }

    public void leaveCommunity() throws IOException {
        // Deleting Participator from assignedTo Dotos
        // Update Community
        this.localCommunity = this.localHandler.getCommunityById(this.localCommunity.getId());
        for(Dotos aDoto : this.localCommunity.getDotosList()) {
            if(aDoto != null && aDoto.getAssignedTo() != null && aDoto.getAssignedTo().equals(this.localParticipator.getUsername())) {
                aDoto.setAssignedTo(null);
            }
        }
        // Persist Community without assignments to the leaving Participator
        this.localHandler.updateCommunity(this.localCommunity.getId(), this.localCommunity.getName(), this.localCommunity.getCreationTime(),
                this.localCommunity.getTaskList(), this.localCommunity.getDotosList());

        // Persist User object with "null" as CommunityId
        this.localHandler.updateParticipator(this.localParticipator.getUsername(), this.localParticipator.getPassword(),
                this.localParticipator.getFirstName(), this.localParticipator.getLastName(), this.localParticipator.getBalance(),
                this.localParticipator.getRole(), "", this.localParticipator.getCreationTime());

        this.message.setSummary("Success (?)");
        this.message.setDetail(String.format("You have left the Community %s - that which also goes by the name of %s",
                this.localCommunity.getId(), this.localCommunity.getName()));
        this.message.setSeverity(FacesMessage.SEVERITY_INFO);
        this.displayMessage();
        this.logout();

    }

    private void displayMessage()
    {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
    }

    private void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "../login.xhtml");


    }

}
