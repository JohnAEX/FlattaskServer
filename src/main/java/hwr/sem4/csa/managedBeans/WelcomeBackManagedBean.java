package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.util.Participator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.PostLoad;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "WelcomeBackManagedBean")
@SessionScoped
public class WelcomeBackManagedBean {


    private String helloProperty = "";

    @PostConstruct
    public void init() {
        String welcomeBack = "Welcome back, " + username();
        this.helloProperty=welcomeBack;
    }

    private String username(){
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            LoginManagedBean login = (LoginManagedBean) session.getAttribute("LoginManagedBean");
            System.out.println("Assigning User: "+login.loggedInUser.getUsername());
            Participator rs = login.loggedInUser;
            return rs.getFirstName();
    }

    public String getHelloProperty() {
        return helloProperty;
    }

    public void setHelloProperty(String helloProperty) {
        this.helloProperty = helloProperty;
    }

}