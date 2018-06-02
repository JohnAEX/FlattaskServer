package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.Generated;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ManagedBean
public class OpenDotosManagedBean {

    // @ManagedProperty("#{LoginManagedBean.username}")
    private String username = "genz_dominik";
    private Participator localPartcipipator;
    private Community localCommunity;

    private DualListModel<Dotos> dotos;
    private Databasehandler localHandler = Databasehandler.instanceOf();

    @PostConstruct
    public void init()
    {
        // fetching context objects
        this.localHandler.initObjectDBConnection();
        this.localPartcipipator = localHandler.getParticipatorByUsername(this.username);
        this.localCommunity = localHandler.getCommunityById(localPartcipipator.getCommunityId());

        //fetching open Dotos objects
        List<Dotos> dotosSource = new ArrayList<Dotos>();
        for(Dotos tempDotos : this.localCommunity.getDotosList()) {
            if(tempDotos.getAssignedTo() == null || tempDotos.getAssignedTo().isEmpty()) {
                dotosSource.add(tempDotos);
            }
        }
        List<Dotos> dotosTarget = new ArrayList<Dotos>();

        this.dotos = new DualListModel<Dotos>(dotosSource, dotosTarget);
    }

    public DualListModel<Dotos> getDotos()
    {
        return this.dotos;
    }

    public void setDotos(DualListModel<Dotos> dotos) {
        this.dotos = dotos;
    }


    public void confirmAssignment()
    {
        System.out.println("Targets: ");
        for(Dotos dotosId : dotos.getTarget()) {
            dotosId.setAssignedTo(this.localPartcipipator.getUsername());
            System.out.println(dotosId.getId() + ": " + dotosId.getTitle());
        }

        this.localHandler.updateCommunity(this.localCommunity.getId(), this.localCommunity.getName(), this.localCommunity.getCreationTime(),
                this.localCommunity.getTaskList(), this.localCommunity.getDotosList());
    }

    public void onTransfer(TransferEvent event)
    {
        System.out.println("I saw that");
        System.out.println("source: " + this.dotos.getSource().size() + " | target: " + this.dotos.getTarget().size());
    }

    @PreDestroy
    public void cleanup()
    {
        this.localHandler.close();
    }
}
