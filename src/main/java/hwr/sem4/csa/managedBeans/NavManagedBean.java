/* Managed Bean for Dashboard Navigation
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean (name="NavManagedBean")
@SessionScoped

public class NavManagedBean {
    /*
    * Insert File-Paths here.
    * Required: Relative Paths, starting from main.xhtml
    * */
    private int navIndex = 0;
    private final String FILE0 = "mytasks.xhtml";
    private final String FILE1 = "dashboard.xhtml";
    private final String FILE2 = "createDoto.xhtml";
    private final String FILE3 = "loading.xhtml";
    private final String FILE4 = "profileSettings.xhtml";
    private final String FILE404 = "dashboard.xhtml";
    private String page = "";

    //Files addressed relativ, remove ... because after login it is all on one level!!!
    public NavManagedBean(){
        this.setPage(0);
        this.getPage();
    } 
    /*Supportive Methode to generate the right Page*/

    public String generatePage(int index){
        System.out.println("NavBean: generatePage: " + index);
        switch(index){
            case 0: return FILE0;
            case 1: return FILE1;
            case 2: return FILE2;
            case 3: return FILE3;
            case 4: return FILE4;
            default: return FILE404;
        }
    }
    /*Getter and Setter */
    public void setPage(int index){
        System.out.println("Detected page setter -> " + index);
        this.navIndex = index;
        this.page = generatePage(index);

    }

    public String getPage(){
        System.out.println("getting Page, current phase: " + FacesContext.getCurrentInstance().getCurrentPhaseId());
        System.out.println("Getting: " + this.page);

        return this.page;
    }

    /* Depracted*/
    public int getNavIndex() {
        System.out.println("Returned navIndex:" + navIndex);
        return navIndex;
    }

    public void setNavIndex(int navIndex) {
        this.navIndex = navIndex;
        System.out.println("Set navIndex:" + this.navIndex);
    }
}
