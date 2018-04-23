package main.java;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class HelloWordManagedBean {


    private String helloProperty = "Hello World !";

    public String getHelloProperty() {
        return helloProperty;
    }

    public void setHelloProperty(String helloProperty) {
        this.helloProperty = helloProperty;
    }

}