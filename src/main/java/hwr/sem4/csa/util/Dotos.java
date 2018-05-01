package hwr.sem4.csa.util;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Dotos implements Serializable{

    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
