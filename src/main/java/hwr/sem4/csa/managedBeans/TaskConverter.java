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
@FacesConverter(value = "TaskConverterFC")
public class TaskConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value){
        System.out.println("Called getASObject");
        return new Task(value,"CREATED BY CONVERTER",69,10);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value){
        System.out.println("Called getASString");
        if(value != null){
            System.out.println("Trying to getAsString from: " + value + " from toString: " + value.toString());
            if(value instanceof Task){
                 return ((Task) value).getTitle();
            }else{
            throw new IllegalArgumentException("Class not of type Task");
             }
        }else{
            return "Value=Null";
        }
    }
}
