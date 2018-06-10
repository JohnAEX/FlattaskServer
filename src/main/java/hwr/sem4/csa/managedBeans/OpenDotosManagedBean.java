package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.comparators.DotosComparator;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ManagedBean
@ViewScoped
public class OpenDotosManagedBean {

    private Participator localParticipator;
    private Community localCommunity;

    private DualListModel<Dotos> dotos;
    private Databasehandler localHandler = Databasehandler.instanceOf();

    private String message;

    @PostConstruct
    public void init()
    {
        // Grab Participator object
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        this.localParticipator = login.getLoggedInUser();

        // Fetching environment objects
        this.localHandler.initObjectDBConnection();
        this.localCommunity = this.localHandler.getCommunityById(this.localParticipator.getCommunityId());

        // Fetching Dotos objects
        List<Dotos> dotosSource = new ArrayList<Dotos>();
        List<Dotos> dotosTarget = new ArrayList<Dotos>();
        for(Dotos tempDotos : this.localCommunity.getDotosList()) {
            if(tempDotos.getAssignedTo() == null || tempDotos.getAssignedTo().isEmpty()) {
                // Open Dotos
                dotosSource.add(tempDotos);
            } else if(tempDotos.getAssignedTo().equals(this.localParticipator.getUsername())) {
                // Assigned Dotos
                dotosTarget.add(tempDotos);
            }
        }

        this.dotos = new DualListModel<Dotos>(dotosSource, dotosTarget);
    }

    public DualListModel<Dotos> getDotos()
    {
        return this.dotos;
    }

    public void setDotos(DualListModel<Dotos> dotos) {
        this.dotos = dotos;
    }

    /**
     * Checks whether the open Dotos for the respective Community have experienced changes since the ManagedBean has been initialized.
     * Only executes the persistency transaction if no changes have been made.
     * Also updates the message field of the ManagedBean with an nformation regarding execution success.
     */
    public void confirmAssignment()
    {
        // Test open DB
        this.localHandler.initObjectDBConnection();

        // Fetch DotoLists
        ArrayList<Dotos> localDotos = this.localCommunity.getDotosList();
        ArrayList<Dotos> remoteDotos = this.localHandler.getCommunityById(this.localCommunity.getId()).getDotosList();

        // Filter to open Dotos
        ArrayList<Dotos> localOpenDotos = new ArrayList<Dotos>(localDotos);
        ArrayList<Dotos> remoteOpenDotos = new ArrayList<Dotos>(remoteDotos);
        localOpenDotos.removeIf(aDoto -> aDoto.getAssignedTo() != null && !aDoto.getAssignedTo().equals(""));
        remoteOpenDotos.removeIf(aDoto -> aDoto.getAssignedTo() != null && !aDoto.getAssignedTo().equals(""));

        if(!this.areEqual(localOpenDotos, remoteOpenDotos)) {
            // Changes have been made
            this.message = "The Open Dotos List of your Community has changed while you were deciding. " +
                    "Your assignments (as well as un-assignments) will not be saved to your Community...";
            return;
        }

        // No changes have been made

        // Initializing requested (un-)assignments
        ArrayList<Dotos> unassignRequest = new ArrayList<Dotos>(this.dotos.getSource());
        unassignRequest.forEach(aDoto -> aDoto.setAssignedTo(null));
        ArrayList<Dotos> assignRequest = new ArrayList<Dotos>(this.dotos.getTarget());
        assignRequest.forEach(aDoto -> aDoto.setAssignedTo(this.localParticipator.getUsername()));

        ArrayList<Dotos> newDotos = new ArrayList<Dotos>(unassignRequest);
        newDotos.addAll(assignRequest);

        // Generate new DotosList
        for(Dotos aDoto : remoteDotos) {
            Optional<Dotos> updatedDoto = newDotos.stream()
                    .filter(anyDoto -> anyDoto.getId() != aDoto.getId())
                    .findAny();
            updatedDoto.ifPresent(iMeanNothing -> newDotos.add(aDoto));
        }

        // Persist updates to Dotos
        this.localHandler.updateCommunity(this.localCommunity.getId(), this.localCommunity.getName(), this.localCommunity.getCreationTime(),
                this.localCommunity.getTaskList(), newDotos);

        this.message = "Your assignments (as well as un-assignments have been saved to your Community...";
    }

    /**
     * Checks two ArrayLists of Type Dotos for equality
     * @param arr1 - first ArrayList to be compared
     * @param arr2 - second ArrayList to be compared
     * @return true if the ArrayLists only contain Dotos-objects with identical fields-values
     */
    private boolean areEqual(ArrayList<Dotos> arr1, ArrayList<Dotos> arr2)
    {
        if(arr1 == null && arr2 == null) {
            return true;
        }
        if(arr1 == null || arr2 == null || arr1.size() != arr2.size()) {
            return false;
        }

        DotosComparator cmp = new DotosComparator();

        for(int i = 0; i < arr1.size(); ++i) {
            if(cmp.compare(arr1.get(i), arr2.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }


    public String getMessage()
    {
        return this.message;
    }
}
