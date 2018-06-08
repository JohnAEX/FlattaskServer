package hwr.sem4.csa.converters;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Optional;

@FacesConverter("hwr.sem4.csa.DotosConverter")
public class DotosConverter implements Converter {
    private Databasehandler localHandler = Databasehandler.instanceOf();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        // Fetch Doto-Containers
        /*
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        Participator localParticipator = login.getLoggedInUser();
        */
        this.localHandler.initObjectDBConnection();
        Participator localParticipator = this.localHandler.getParticipatorByUsername("genz_dominik");
        Community localCommunity = localHandler.getCommunityById(localParticipator.getCommunityId());
        this.localHandler.close();

        // Lookup the requested Doto
        Optional<Dotos> optional = localCommunity.getDotosList().stream()
                .filter(dotos -> dotos.getId() == Long.parseLong(value))
                .findFirst();

        if(!optional.isPresent()) {
            // No Doto found
            return null;
        }

        return optional.get();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if(!(value instanceof Dotos)) {
            // No Doto-instance
            return null;
        }

        return String.valueOf(((Dotos) value).getId());
    }

}
