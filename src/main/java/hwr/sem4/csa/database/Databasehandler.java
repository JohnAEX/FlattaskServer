package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Participator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Databasehandler {
    static Databasehandler thisDBHandler = new Databasehandler();
    private EntityManagerFactory emFactory;

    private Databasehandler() {    }

    static public Databasehandler instanceOf() {
        return thisDBHandler;
    }

    public void initObjectDBConnection(){
        emFactory = Persistence.createEntityManagerFactory("objectdb:" +
                "//ec2-54-85-66-232.compute-1.amazonaws.com:6136/real.odb;user=admin;password=admin");
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public void close() {
        emFactory.close();
    }

    //Inserts passed Object into ODB; no checks are made
    public void insert(Object toInsert){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(toInsert);
        em.getTransaction().commit();
        em.close();
    }

    //Clears DB, currently needed for JUNIT
    public void clearAll(){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Community").executeUpdate();
        em.createQuery("DELETE FROM Participator").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    //Tries to grab exactly one community by ID, if more than one/or none with the id exist -> throws error
    public Community getCommunityById(String id){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Community> typedResultQuery = em.createQuery("SELECT c FROM Community c WHERE c.id = :id",Community.class);
        List<Community> resultList = typedResultQuery.setParameter("id",id).getResultList();
        if(resultList.size()==1){
            return resultList.get(0);
        }else{
            return null;
        }


    }

    public List<Participator> getParticipatorsByCommunityID(String id){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Participator> typedResultQuery = em.createQuery("SELECT p FROM Participator p WHERE p.id = :id",Participator.class);
        List<Participator> resultList = typedResultQuery.setParameter("id",id).getResultList();
        if(resultList.size()==1){
            return resultList;
        }else{
            return null;
        }
    }

    //Tries to grab exactly one participator by username, if more than one/or none with the id exist -> throws error
    public Participator getParticipatorByUsername(String username){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Participator> typedResultQuery = em.createQuery("SELECT p FROM Participator p WHERE p.username = :username", Participator.class);
        try{
            Participator resultParticipator = typedResultQuery.setParameter("username",username).getSingleResult();
            return resultParticipator;
        }catch(Exception e){
            System.out.println("Error in Method: getParticipatorByUsername");
            e.printStackTrace();
            return null;
        }
    }

    //special login-method to prevent any false-login attempts
    public Participator getParticipatorByLogin(String username, String password){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Participator> typedResultQuery = em.createQuery("SELECT p FROM Participator p WHERE " +
                "p.username = :username AND p.password = :password",Participator.class);
        List<Participator> participatorList = typedResultQuery.setParameter("username",username)
                .setParameter("password", password).getResultList();
        if(participatorList.size() == 1){
            return participatorList.get(0);
        }else {
            return null;
        }


    }




}

