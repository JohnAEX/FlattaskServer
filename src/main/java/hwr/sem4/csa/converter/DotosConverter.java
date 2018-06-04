package hwr.sem4.csa.converter;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;

import javax.annotation.PreDestroy;
import javax.faces.annotation.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Optional;

@FacesConverter("hwr.sem4.csa.DotosConverter")
public class DotosConverter implements Converter {

    // @ManagedProperty("#{LoginManagedBean.username}")
    private String username = "genz_dominik";

    private Databasehandler localHandler = Databasehandler.instanceOf();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        this.localHandler.initObjectDBConnection();
        Optional<Community> containingCommunity = Optional.of(this.localHandler.getCommunityById(this.localHandler.getParticipatorByUsername(this.username).getCommunityId()));

        if(!containingCommunity.isPresent()) {
            // Log this
            return null;
        }

        Optional<Dotos> optional = containingCommunity.get().getDotosList().stream()
                .filter(dotos -> dotos.getId() == Long.parseLong(value))
                .findFirst();

        if(!optional.isPresent()) {
            // Log this
            return null;
        }

        return optional;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if(!(value instanceof Dotos)) {
            // Log this
            return null;
        }

        return String.valueOf(((Dotos) value).getId());
    }

    @PreDestroy
    public void cleanup()
    {
        this.localHandler.close();
    }
}
