/* Managed Bean for Dashboard Navigation
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Participator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean (name="CreateTaskManagedBean")
@SessionScoped

public class CreateTaskManagedBean {
    /*
    * Attributes
    * */
    private String title = "";
    private String description = "";
    private String userAssign = "";
    private String userAssigned = "";
    private int value = 0;
    private int duration = 0;
    private int valueMax = 5;
    private String[] usersPossible = {};
    private Databasehandler database = Databasehandler.instanceOf();


    /*
    * Constructor
    * */
    public CreateTaskManagedBean(){

    }

    /*Functional Methods*/

    public String[] searchForPossibleUsers(){
        Participator part = database.getParticipatorByUsername(this.userAssign);
        List<Participator> possiblePart = database.getParticipatorsByCommunityID(part.getCommunityId());
        String[] returnStringArray = {};
        if(possiblePart.size() > 0) {
            for (int i = 0; i < possiblePart.size(); i++) {
                returnStringArray[i] = possiblePart.get(i).getFirstName() + " " + possiblePart.get(i).getLastName();
            }
        }
        else{
            returnStringArray[0] = "Please add Participators to your Community";
        }
        return returnStringArray;

    }

    public String generateHTMLPossibleUsers(){
        if (this.usersPossible.length>0){
            String htmlCode ="";
            for (int i = 0; i<this.usersPossible.length; i++){
                htmlCode = htmlCode + "";
            }
            return htmlCode;
        }
        return "";

    }

    public void confirmTask(){

    }

    /*Getter & Setter*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserAssign() {
        return userAssign;
    }

    public void setUserAssign(String userAssign) {
        this.userAssign = userAssign;
    }

    public String getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(String userAssigned) {
        this.userAssigned = userAssigned;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }

    public String[] getUsersPossible() {
        return usersPossible;
    }

    public void setUsersPossible(String[] usersPossible) {
        this.usersPossible = usersPossible;
    }
}
