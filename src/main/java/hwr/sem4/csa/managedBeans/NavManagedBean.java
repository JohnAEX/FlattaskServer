/* Managed Bean for Dashboard Navigation
* Author: LM
* */

package hwr.sem4.csa.managedBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name="NavManagedBean")
@SessionScoped

public class NavManagedBean {
    /*
    * Insert File-Paths here.
    * Required: Relative Paths, starting from main.xhtml
    * */
    private int navIndex = 0;
    private final String FILE0 = "loading.xhtml";
    private final String FILE1 = "index.xhtml";
    private final String FILE2 = "loading.xhtml";
    private final String FILE3 = "dashboard.xhtml";
    private final String FILE4 = "dashboard.xhtml";
    private final String FILE404 = "dashboard.xhtml";
    private String page = "";

    //Files addressed relativ, remove ... because after login it is all on one level!!!
    public NavManagedBean(){
        this.setPage(0);
        this.getPage();
    } 
    /*Supportive Methode to generate the right Page*/

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
    /*Getter and Setter */
    public void setPage(int index){
        System.out.println("Detected page setter -> " + index);
        this.page = generatePage(index);

    }

    public String getPage(){
        System.out.println(this.page);
        return this.page;
    }

    /* Depracted*/
    public int getNavIndex() {
        return navIndex;
    }

    public void setNavIndex(int navIndex) {
        this.navIndex = navIndex;
    }
}
