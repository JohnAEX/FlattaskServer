package hwr.sem4.csa.util;

import java.util.ArrayList;

public class DummyTaskGenerator {

     private ArrayList<Task> dummyTasks;

     public DummyTaskGenerator(){
           this.setDummyTasks(dummyTaskGeneration());
     }

     private static ArrayList<Task> dummyTaskGeneration(){
            ArrayList<Task> zs = new ArrayList<>();
            //String title, String description, int baseValue, int baseDuration
            zs.add(new Task("Bier holen", "Zum Supermarkt laufen und eine Kiste Bölkstoff besorgen.", 20, 60));
            zs.add(new Task("Bad putzen", "Bitte einmal das komplette Bad putzen", 30, 90));
            zs.add(new Task("Müll runter bringen", "Bitte den Müll herunterbringen, ohne die Tüte auf dem Weg aufreißen zu lassen", 5, 10));
            return zs;
     }

     public ArrayList<Task> getDummyTasks() {
            return dummyTasks;
     }

     public void setDummyTasks(ArrayList<Task> dummyTasks) {
            this.dummyTasks = dummyTasks;
     }
}
