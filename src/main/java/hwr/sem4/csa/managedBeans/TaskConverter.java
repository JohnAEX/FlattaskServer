package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Task;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

// You must annotate the converter as a managed bean, if you want to inject
// anything into it, like your persistence unit for example.
@ManagedBean(name = "TaskConverterBean")
@FacesConverter(value = "TaskConverter")
public class TaskConverter implements Converter {


    private transient EntityManager em;


    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component,
                              String value) {
        // This will return the actual object representation
        // of your Category using the value (in your case 52)
        // returned from the client side
        return em.find(Task.class, new String(value));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        //This will return view-friendly output for the dropdown menu
        return ((Task) o).getId().toString();
    }
}
