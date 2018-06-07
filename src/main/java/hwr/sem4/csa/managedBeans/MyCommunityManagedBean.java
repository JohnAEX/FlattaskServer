package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.comparators.ParticipatorFlattyComparator;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Participator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ManagedBean
public class MyCommunityManagedBean {

    private Databasehandler localHandler = Databasehandler.instanceOf();

    private Participator localParticipator;
    private Community localCommunity;

    // Properties exclusively for Primeface elements
    private List<Participator> participatorList;

    private Participator selectedParticipator;
    private String message;

    @PostConstruct
    public void init()
    {
        /*
        // Grab Participator object
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        this.localParticipator = login.getLoggedInUser();
        */
        this.localHandler.initObjectDBConnection();
        this.localParticipator = this.localHandler.getParticipatorByUsername("genz_dominik");

        // Fetch Participator List
        Optional<Community> optional = Optional.of(localHandler.getCommunityById(localParticipator.getCommunityId()));
        if(optional.isPresent()) {
            this.localCommunity = optional.get();
            this.participatorList = this.localHandler.getParticipatorsByCommunityID(this.localCommunity.getId());
            this.participatorList.sort(new ParticipatorFlattyComparator());
        } else {
            this.localCommunity = null;
            this.participatorList = null;
            this.message = "Your Community could not be found.";
        }
    }

    public void setParticipatorList(List<Participator> participatorList)
    {
        this.participatorList = participatorList;
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
}