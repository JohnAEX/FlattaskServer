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
import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class MyTasksManagedBean {

    public static final int DEFAULT_COLUMN_COUNT = 3;
    private int columnCount = DEFAULT_COLUMN_COUNT;
    private ArrayList<Panel> panelCollector = new ArrayList<>();
    private Participator loggedInUser;
    private ArrayList<Dotos> actualUserDotosList = new ArrayList<>();

    private Dashboard dashboard;

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();

        dashboard = (Dashboard) application.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
        dashboard.setId("dashboard");

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
            int items = actualUserDotosList.size();

            for( int i = 0; i < items; i++ ) {
                //System.out.println("Trying to access panel: i=" + i + " items=" + items);
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

                panel.getChildren().add(text);
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

    public void handleComplete(String id){
        Panel completedPanel = findPanel(id);

        System.out.println("Completed: " + completedPanel.getHeader());

        Databasehandler.instanceOf().initObjectDBConnection();
        Community c = Databasehandler.instanceOf().getCommunityById(loggedInUser.getCommunityId());
        ArrayList<Dotos> cDList = c.getDotosList();
        boolean foundTask = false;
        try{
            for(Dotos d : cDList){
                if(d.getTitle().equals(completedPanel.getHeader()) && d.getAssignedTo().equals(loggedInUser.getUsername())){
                    loggedInUser.setBalance(loggedInUser.getBalance()+d.getValue());
                    cDList.remove(d);
                    actualUserDotosList.remove(d);
                    foundTask = true;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Task could not be completetd. Error code #001");
        }

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

    public void handleCancel(String id){
        Panel completedPanel = findPanel(id);

        System.out.println("Canceled: " + completedPanel.getHeader());

        Databasehandler.instanceOf().initObjectDBConnection();
        Community c = Databasehandler.instanceOf().getCommunityById(loggedInUser.getCommunityId());
        ArrayList<Dotos> cDList = c.getDotosList();
        boolean foundTask = false;
        try{
            for(Dotos d : cDList){
                if(d.getTitle().equals(completedPanel.getHeader()) && d.getAssignedTo().equals(loggedInUser.getUsername())){
                    cDList.remove(d);
                    actualUserDotosList.remove(d);
                    foundTask = true;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Task could not be canceled. Error code #002");
        }

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

    public void refresh(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Panel findPanel(final String id) {
        boolean found = false;
        System.out.println("FIND: " + id);
        Panel completedPanel = new Panel();
        for(Panel p : panelCollector){
            for(UIComponent uic : p.getChildren()){
                System.out.println("\t" + uic.getId());
                for(UIComponent uic2 : uic.getChildren()){
                    System.out.println("\t\t"+uic2.getId());
                    for(UIComponent uic3 : uic2.getChildren()) {
                        System.out.println("\t\t\t" + uic3.getId());
                        for(UIComponent uic4 : uic3.getChildren()){
                            System.out.println("\t\t\t\t" + uic3.getId());
                            if (uic4.getId().equals(id)) {
                                System.out.println("\t\t\t\t\tFOUND");
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
