package hwr.sem4.csa.managedBeans;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

public class DashboardManagedBean {
    private DashboardModel model;

    public DashboardManagedBean(){
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

        column1.addWidget("Tasks");
        column2.addWidget("Settings");

        model.addColumn(column1);
        model.addColumn(column2);

    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }
}
