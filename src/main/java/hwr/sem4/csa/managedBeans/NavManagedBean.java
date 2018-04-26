package hwr.sem4.csa.managedBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name="NavManagedBean")
@SessionScoped

public class NavManagedBean {
    private int navIndex = 0;
    private final String FILE0 = "login.xhtml";
    private final String FILE1 = "index.xhtml";
    private final String FILE2 = "dashboard.xhtml";
    private final String FILE3 = "dashboard.xhtml";
    private final String FILE4 = "dashboard.xhtml";
    private final String FILE404 = "dashboard.xhtml";
    private String page = "";

    public int getNavIndex() {
        return navIndex;
    }

    public void setNavIndex(int navIndex) {
        this.navIndex = navIndex;
    }



    public String generatePage(int index){
        switch(index){
            case 0: return FILE0;
            case 1: return FILE1;
            case 2: return FILE2;
            case 3: return FILE3;
            case 4: return FILE4;
            default: return FILE404;
        }
    }

    public void setPage(int index){
        this.page = generatePage(index);
    }

    public String getPage(){
        return this.page;
    }
}
