package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

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
                "//ec2-54-85-66-232.compute-1.amazonaws.com:6136/systemTest.odb;user=admin;password=admin");
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

    public void insertList(List list){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        for(Object o : list){
            em.persist(o);
        }
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
        TypedQuery<Participator> typedResultQuery = em.createQuery("SELECT p FROM Participator p WHERE p.communityId = :id", Participator.class);
        List<Participator> resultList = typedResultQuery.setParameter("id",id).getResultList();
        if(resultList.size()>=1){
            return resultList;
        }else{
            //No User found with the given ID
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

    //Update Methods
    public void updateParticipator(String username, String password, String firstName, String lastname, int balance,
                                   String role, String communityID, String creationTime){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        Query newQuery = em.createQuery("UPDATE Participator p SET p.password = :password, " +
                "p.firstName = :firstName, p.lastName = :lastName, p.balance = :balance, " +
                "p.role = :role, p.communityId = :communityID, p.creationTime = :creationTime WHERE p.username = :username", Participator.class);
        newQuery.setParameter("password",password);
        newQuery.setParameter("firstName",firstName);
        newQuery.setParameter("lastName",lastname);
        newQuery.setParameter("balance",balance);
        newQuery.setParameter("role",role);
        newQuery.setParameter("communityID",communityID);
        newQuery.setParameter("creationTime",creationTime);
        newQuery.setParameter("username",username);
        newQuery.executeUpdate();
        em.getTransaction().commit();
        em.close();

    }

    public void updateCommunity(String id, String name, String creationTime, ArrayList<Task> taskList, ArrayList<Dotos> dotosList){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
       /* em.persist(taskList);
        em.persist(dotosList);*/
        Query newQuery = em.createQuery("UPDATE Community c SET c.name = :name, c.creationTime = :creationTime, " +
                "c.taskList = :taskList, c.dotosList = :dotosList " +
                "WHERE c.id = :id");
        newQuery.setParameter("name",name);
        newQuery.setParameter("creationTime",creationTime);
        newQuery.setParameter("id",id);
        newQuery.setParameter("taskList",taskList);
        newQuery.setParameter("dotosList", dotosList);
        newQuery.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    //SysAdmin methods
    public List<Participator> getAllParticipators(){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Participator> typedQuery = em.createQuery("SELECT p FROM Participator p",Participator.class);
        return typedQuery.getResultList();
    }

    public List<Community> getAllCommunities(){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Community> typedQuery = em.createQuery("SELECT c FROM Community c",Community.class);
        return typedQuery.getResultList();
    }

    public void removeParticipatorByUsername(String username){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        Query currentQuery = em.createQuery("DELETE FROM Participator p WHERE p.username = :username", Participator.class);
        currentQuery.setParameter("username",username).executeUpdate();
        em.getTransaction().commit();
        em.close();

    }


    public void removeCommunityById(String id){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        Query currentQuery = em.createQuery("DELETE FROM Community c WHERE c.id = :id", Community.class);
        currentQuery.setParameter("id",id).executeUpdate();
        em.getTransaction().commit();
        em.close();
    }




}

