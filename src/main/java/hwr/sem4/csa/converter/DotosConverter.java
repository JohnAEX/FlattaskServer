package hwr.sem4.csa.converter;

import hwr.sem4.csa.util.Dotos;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("hwr.sem4.csa.DotosConverter")
public class DotosConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        Dotos dotosBuilder = new Dotos();
        try{
            String[] dotosProperties = value.split(String.valueOf('\7'));


            dotosBuilder.setId(Integer.parseInt(dotosProperties[0]));
            dotosBuilder.setTitle(dotosProperties[1]);
            dotosBuilder.setDescription(dotosProperties[2]);
            dotosBuilder.setValue(Integer.valueOf(dotosProperties[3]));
            dotosBuilder.setDuration(Integer.valueOf(dotosProperties[4]));
            dotosBuilder.setAssignedTo(dotosProperties[5]);
            dotosBuilder.setAssignedBy(dotosProperties[6]);
        } catch(Exception exc) {
            return null;
        }
        return dotosBuilder;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if(!(value instanceof Dotos)) {
            // Log this
            return null;
        }

        Dotos localDotos = (Dotos) value;
        StringBuilder builder = new StringBuilder();

        builder.append(localDotos.getId() + '\7');
        if(localDotos.getTitle() == null) {
            builder.append('\7');
        } else {
            builder.append(localDotos.getTitle() + '\7');
        }
        if(localDotos.getDescription() == null) {
            builder.append('\7');
        } else {
            builder.append(localDotos.getDescription() + '\7');
        }
        builder.append(localDotos.getValue() + '\7');
        builder.append(localDotos.getDuration() + '\7');
        if(localDotos.getAssignedTo() == null) {
            builder.append('\7');
        } else {
            builder.append(localDotos.getAssignedTo() + '\7');
        }
        if(localDotos.getAssignedBy() == null) {
            builder.append('\7');
        } else {
            builder.append(localDotos.getAssignedBy());
        }

        return builder.toString();
    }
}
