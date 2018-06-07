package hwr.sem4.csa.converters;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Optional;

@FacesConverter("hwr.sem4.csa.converters.ParticipatorConverter")
public class ParticipatorConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        // Open DB-Connection
        Databasehandler localHandler = Databasehandler.instanceOf();
        localHandler.initObjectDBConnection();

        // Fetch and return Participator object
        return  localHandler.getParticipatorByUsername(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if(!(value instanceof Participator)) {
            // No Doto-instance
            return null;
        }

        return ((Participator) value).getUsername();
    }

}
