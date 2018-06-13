package hwr.sem4.csa.managedBeans;

import com.sun.faces.component.visit.FullVisitContext;
import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import org.primefaces.component.button.Button;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
Code provided in combinbation with
http://www.naturalborncoder.com/java/java-ee/2011/11/22/dynamic-dashboard-with-primefaces/
 */

@ManagedBean
@ViewScoped
public class MyTasksManagedBean {
    /*
     * Used to display the users tasks and give the user the option to abort or complete them
     */

    public static final int DEFAULT_COLUMN_COUNT = 3;
    private int columnCount = DEFAULT_COLUMN_COUNT;
    private ArrayList<Panel> panelCollector = new ArrayList<>();
    private Participator loggedInUser;
    private ArrayList<Dotos> actualUserDotosList = new ArrayList<>();

    private Dashboard dashboard;

    //PostConstruct used to make sure the init-method runs without errors after the construction of the bean
    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();
        //Create Dashboard component
        dashboard = (Dashboard) application.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
        dashboard.setId("dashboard");

        //Add elements to the Dashboard
        DashboardModel model = new DefaultDashboardModel();
        for( int i = 0, n = getColumnCount(); i < n; i++ ) {
            DashboardColumn column = new DefaultDashboardColumn();
            model.addColumn(column);
        }
        dashboard.setModel(model);

        /*
            Get All DoTos for the logged-in user
            create that many components as panels
         */
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
        loggedInUser = login.getLoggedInUser();
        Databasehandler.instanceOf().initObjectDBConnection();
        Community loggedInUserCommunity = Databasehandler.instanceOf().getCommunityById(loggedInUser.getCommunityId());
        ArrayList<Dotos> allDotosList = loggedInUserCommunity.getDotosList();

        for(Dotos d : allDotosList){
            if(d != null && d.getAssignedTo() != null) {
                if (d.getAssignedTo().equals(loggedInUser.getUsername())) {
                    actualUserDotosList.add(d);
                }
            }
        }
        if(actualUserDotosList.size()>0){
            //Making sure the User has Dotos to complete
            int items = actualUserDotosList.size();

            //Adding Panels and Properties
            for( int i = 0; i < items; i++ ) {
                Panel panel = (Panel) application.createComponent(fc, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
                panel.setId("measure_" + i);
                panel.setHeader(actualUserDotosList.get(i).getTitle());
                panel.setClosable(false);
                panel.setToggleable(true);



                getDashboard().getChildren().add(panel);
                DashboardColumn column = model.getColumn(i%getColumnCount());
                column.addWidget(panel.getId());
                HtmlOutputText text = new HtmlOutputText();
                text.setValue(actualUserDotosList.get(i).getDescription());

                HtmlOutputText text2 = new HtmlOutputText();
                text2.setEscape(false);
                text2.setValue("<br/>" + "Worth: " + actualUserDotosList.get(i).getValue());

                panel.getChildren().add(text);
                panel.getChildren().add(text2);
                FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
                try {
                    faceletContext.includeFacelet(panel, "buttonTest.xhtml");
                    panelCollector.add(panel);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else{
            //No Current Tasks
            //Show message
        }

        Databasehandler.instanceOf().close();

    }

    //Method to handle the completion of a Task
    public void handleComplete(String id){
        Panel completedPanel = findPanel(id);

        System.out.println("Completed: " + completedPanel.getHeader()); //Debugging output
        //Now trying to find what task has been completed
        Databasehandler.instanceOf().initObjectDBConnection();
        Community c = Databasehandler.instanceOf().getCommunityById(loggedInUser.getCommunityId());
        ArrayList<Dotos> cDList = c.getDotosList();
        boolean foundTask = false;
        try{
            for(Dotos d : cDList){
                //Checking based on Header an assigned user in Community
                if(d.getTitle().equals(completedPanel.getHeader()) && d.getAssignedTo().equals(loggedInUser.getUsername())){

                    loggedInUser.setBalance(loggedInUser.getBalance()+d.getValue());
                    cDList.remove(d);
                    actualUserDotosList.remove(d);
                    foundTask = true;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Task could not be completetd. Error code #001"); //Error output
        }
        //If Task was found, updating the User with the new currency
        if(foundTask){
            System.out.println("Removing task from community and updating participator");
            Databasehandler.instanceOf().updateCommunity(c.getId(),c.getName(),c.getCreationTime(),c.getTaskList(),c.getDotosList());
            Databasehandler.instanceOf().updateParticipator(loggedInUser.getUsername(),loggedInUser.getPassword(),
                    loggedInUser.getFirstName(),loggedInUser.getLastName(),loggedInUser.getBalance(),
                    loggedInUser.getRole(),loggedInUser.getCommunityId(),loggedInUser.getCreationTime());
        }

        Databasehandler.instanceOf().close();
        refresh();

    }

    //Method to handle the cancelation of a Task
    public void handleCancel(String id){
        int toRefund = 0;
        Participator toGetRefund = null;
        Panel completedPanel = findPanel(id);

        System.out.println("Canceled: " + completedPanel.getHeader()); //Debugging output
        //Trying to find what Task has been canceled
        Databasehandler.instanceOf().initObjectDBConnection();
        Community c = Databasehandler.instanceOf().getCommunityById(loggedInUser.getCommunityId());
        ArrayList<Dotos> cDList = c.getDotosList();
        boolean foundTask = false;
        try{
            for(Dotos d : cDList){
                //Checking based on Header an assigned user in Community
                if(d.getTitle().equals(completedPanel.getHeader()) && d.getAssignedTo().equals(loggedInUser.getUsername())){
                    toGetRefund = Databasehandler.instanceOf().getParticipatorByUsername(d.getAssignedBy());
                    toRefund = d.getValue();
                    cDList.remove(d);
                    actualUserDotosList.remove(d);
                    foundTask = true;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Task could not be canceled. Error code #002"); //Error output
        }
        //If Task was found, updating user as well as community to remove Task
        if(foundTask){
            System.out.println("Removing task from community and updating participator");
            Databasehandler.instanceOf().updateCommunity(c.getId(),c.getName(),c.getCreationTime(),c.getTaskList(),c.getDotosList());
            Databasehandler.instanceOf().updateParticipator(loggedInUser.getUsername(),loggedInUser.getPassword(),
                    loggedInUser.getFirstName(),loggedInUser.getLastName(),loggedInUser.getBalance(),
                    loggedInUser.getRole(),loggedInUser.getCommunityId(),loggedInUser.getCreationTime());
            Databasehandler.instanceOf().updateParticipator(toGetRefund.getUsername(),toGetRefund.getPassword(),toGetRefund.getFirstName(),
                    toGetRefund.getLastName(),toGetRefund.getBalance()+toRefund,toGetRefund.getRole(),toGetRefund.getCommunityId(),
                    toGetRefund.getCreationTime());

        }

        Databasehandler.instanceOf().close();
        refresh();
    }

    //Method used to force a refresh once called
    public void refresh(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Due to the code creation of xhtml elements it is necessary to have a Method to check for
     * all the Elements to find the Task that has been completed / canceled. This is very unsafe
     * an should be Nr. 1 priority to rework once a better solution has been found!
     */
    public Panel findPanel(final String id) {
        boolean found = false;
        System.out.println("FIND: " + id); //Debugging output
        Panel completedPanel = new Panel();
        for(Panel p : panelCollector){
            for(UIComponent uic : p.getChildren()){
                System.out.println("\t" + uic.getId());//Debugging output
                for(UIComponent uic2 : uic.getChildren()){
                    System.out.println("\t\t"+uic2.getId());//Debugging output
                    for(UIComponent uic3 : uic2.getChildren()) {
                        System.out.println("\t\t\t" + uic3.getId());//Debugging output
                        for(UIComponent uic4 : uic3.getChildren()){
                            System.out.println("\t\t\t\t" + uic3.getId());//Debugging output
                            if (uic4.getId().equals(id)) {
                                System.out.println("\t\t\t\t\tFOUND");//Debugging output
                                completedPanel = (Panel) uic4.getParent().getParent().getParent().getParent();
                                found = true;
                                break;
                            }
                        }if(found){break;}



                    }
                    if(found){break;}
                }
                if(found){break;}

            }
            if(found){break;}
        }



        return completedPanel;

    }

    public Participator getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Participator loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ArrayList<Dotos> getActualUserDotosList() {
        return actualUserDotosList;
    }

    public void setActualUserDotosList(ArrayList<Dotos> actualUserDotosList) {
        this.actualUserDotosList = actualUserDotosList;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
