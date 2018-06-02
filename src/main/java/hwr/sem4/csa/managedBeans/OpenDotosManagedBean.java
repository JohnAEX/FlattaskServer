package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class OpenDotosManagedBean {

    // @ManagedProperty("#{LoginManagedBean.username}")
    private String username = "genz_dominik";
    private Participator localPartcipipator;
    private Community localCommunity;

    private DualListModel<String> dotos;
    private Databasehandler localHandler = Databasehandler.instanceOf();

    @PostConstruct
    public void init()
    {
        // fetching context objects
        this.localHandler.initObjectDBConnection();
        this.localPartcipipator = localHandler.getParticipatorByUsername(this.username);
        this.localCommunity = localHandler.getCommunityById(localPartcipipator.getCommunityId());

        //fetching open Dotos objects
        List<String> dotosSource = new ArrayList<String>();
        for(Dotos tempDotos : this.localCommunity.getDotosList()) {
            String tempDesc = tempDotos.getDescription();
            if(tempDesc == null) {
                tempDesc = "Sample Desc.";
            }else if(tempDesc.length() >= 10) {
                tempDesc = tempDesc.substring(0, 10) + "...";
            }
            dotosSource.add(tempDotos.getTitle() + "(" + tempDesc + "...)");
        }
        List<String> dotosTarget = new ArrayList<String>();
        this.dotos = new DualListModel<String>(dotosSource, dotosTarget);

    }

    public DualListModel<String> getDotos()
    {
        return this.dotos;
    }

    public void setDotos(DualListModel<String> dotos) {
        this.dotos = dotos;
    }


}
