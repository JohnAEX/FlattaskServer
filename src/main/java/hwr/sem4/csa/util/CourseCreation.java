package hwr.sem4.csa.util;

import hwr.sem4.csa.database.Databasehandler;

import java.util.ArrayList;

public class CourseCreation {

    public static void main(String [] args){
        ArrayList<Participator> b = genUsers();
        Databasehandler db = Databasehandler.instanceOf();
        for (int i=0; i<b.size(); i++){
            db.insert(b.get(i));
        }
    }

  // public Participator(String username, String password, String firstName, String lastName, int balance, String role, String communityId) {

    public static ArrayList<Participator> genUsers() {
        ArrayList<Participator> a = new ArrayList<>();
        a.add(new Participator("guhl_alicia", "guhl_alicia", "Alicia", "Guhl", 50, "user", "CSA"));
        a.add(new Participator("schmietendorf_andreas", "schmietendorf_andreas", "Andreas", "Schmietendorf", 50, "user", "CSA"));
        a.add(new Participator("schachter_arne", "schater_arne", "Arne", "Schachter", 50, "user", "CSA"));
        a.add(new Participator("pahlitzsch_benjamin", "pahlitzsch_benjamin", "Benjamin", "Pahlitzsch", 50, "user", "CSA"));
        a.add(new Participator("uzman_canay", "uzman_canay", "Canay", "Uzman", 50, "user", "CSA"));
        a.add(new Participator("bohn_christoph", "bohn_christoph", "Christoph", "Bohn", 50, "user", "CSA"));
        a.add(new Participator("cetinkaya_coskun", "cetinkaya_coskun", "Coskun", "Cetinkaya", 50, "user", "CSA"));
        a.add(new Participator("standke_daniel", "standke_daniel", "Daniel", "Standke", 50, "user", "CSA"));
        a.add(new Participator("mann_darius", "mann_darius", "Darius", "Mann", 50, "user", "CSA"));
        a.add(new Participator("langefeld_domenik", "langefeld_domenik", "Dominik", "Langefeld", 50, "user", "CSA"));
        a.add(new Participator("genz_dominik", "genz_dominik", "Dominik", "Genz", 50, "user", "CSA"));
        a.add(new Participator("tsakonas_felicia", "tsakonas_felicia", "Felicia", "Tsakonas", 50, "user", "CSA"));
        a.add(new Participator("kipka_fenja", "kipka_fenja", "Fenja", "Kipka", 50, "user", "CSA"));
        a.add(new Participator("gerber_florian", "gerber_florian", "Florian", "Gerber", 50, "user", "CSA"));
        a.add(new Participator("grenda_jonathan", "grenda_jonathan", "Jonathan", "Grenda", 50, "user", "CSA"));
        a.add(new Participator("ramer_luis", "ramer_luis", "Luis", "Ramer", 50, "user", "CSA"));
        a.add(new Participator("klöpping_lukas", "klöpping_lukas", "Lukas", "Klöpping", 50, "user", "CSA"));
        a.add(new Participator("rech_madlin", "rech_madlin", "Madlin", "Rech", 50, "user", "CSA"));
        a.add(new Participator("sowada_marcel", "sowada_marcel", "Marcel", "Sowada", 50, "user", "CSA"));
        a.add(new Participator("kollar_mark", "koller_mark", "Márk", "Kollár", 50, "user", "CSA"));
        a.add(new Participator("menz_maximilian", "menz_maximilian", "Maximilian", "Menz", 50, "user", "CSA"));
        a.add(new Participator("hauska_nico", "hauska_nico", "Nico", "Hauska", 50, "user", "CSA"));
        a.add(new Participator("vonholdt_pascal", "vonholdt_pascal", "Pascal", "Vonholdt", 50, "user", "CSA"));
        a.add(new Participator("küchler-schäfer_paul", "küchler-schäfer_paul", "Paul", "Küchler-Schäfer", 50, "user", "CSA"));
        a.add(new Participator("ruhkieck_sophia", "ruhkieck_sophia", "Sophia", "Ruhkieck", 50, "user", "CSA"));
        a.add(new Participator("manlik_tanja", "manlik_tanja", "Tanja", "Manlik", 50, "user", "CSA"));
        a.add(new Participator("kamprath_tom", "kamprath_tom", "Tom", "Kamprath", 50, "user", "CSA"));
        a.add(new Participator("scheer_tristan", "scheer_tristan", "Tristan", "Scheer", 50, "user", "CSA"));
        return a;
    }

 /*   public void defineUsername(String name){
        http.get("https://tinyurl.com/flattask");
        switch name{
            case "Vorname Nachname": username = password = nachname_vorname; break;
            case "Vorname Vorname Nachname": this.ignorePrename2(); defineUsername(newName()); break;
            case "Vorname Nachname-Nachname": username = password = nachname-nachname_vorname; break;
            case "Vornáme Nächname": this.ignoreSpecialDigits(); defineUsername(newName()); break;
            case "Vorüikslös Naciogr": this.blameDeveloper("Lucas"); this.getExcuses();
            default: this.haveFun();
        }
    }*/

}
