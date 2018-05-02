package hwr.sem4.csa.managedBeans;

import com.sun.faces.component.visit.FullVisitContext;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.view.facelets.FaceletContext;
import java.io.IOException;
import java.util.ArrayList;

/*
Code provided in combinbation with
http://www.naturalborncoder.com/java/java-ee/2011/11/22/dynamic-dashboard-with-primefaces/
 */

@ManagedBean
@ViewScoped
public class MyTasksManagedBean {

    public static final int DEFAULT_COLUMN_COUNT = 3;
    private int columnCount = DEFAULT_COLUMN_COUNT;
    private ArrayList<Panel> panelCollector = new ArrayList<>();

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

        int items = 5;

        for( int i = 0, n = items; i < n; i++ ) {
            Panel panel = (Panel) application.createComponent(fc, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
            panel.setId("measure_" + i);
            panel.setHeader("Dashboard Component " + i);
            panel.setClosable(false);
            panel.setToggleable(true);

            CommandButton complete = new CommandButton();
            complete.setValue("Complete");
            complete.setIcon("fa fa-money");
            complete.setUpdate("dynamicDashboard");



            getDashboard().getChildren().add(panel);
            DashboardColumn column = model.getColumn(i%getColumnCount());
            column.addWidget(panel.getId());
            HtmlOutputText text = new HtmlOutputText();
            text.setValue("Dashboard widget bits!" );

            panel.getChildren().add(text);
            FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
            try {
                faceletContext.includeFacelet(panel, "buttonTest.xhtml");
                panelCollector.add(panel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //panel.getChildren().add(complete);
        }
    }

    public void handleComplete(String id){
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
                            if (uic3.getId().equals(id)) {
                                System.out.println("\t\t\t\tFOUND");
                                completedPanel = (Panel) uic3.getParent().getParent().getParent();
                                found = true;
                                break;
                            }
                        }
                     if(found){break;}
                }
                if(found){break;}

            }
            if(found){break;}
        }

        System.out.println("Completed: " + completedPanel.getHeader());

    }

    public UIComponent findComponent(final String id) {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        root.visitTree(new FullVisitContext(context), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if(component.getId().equals(id)){
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return found[0];

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
