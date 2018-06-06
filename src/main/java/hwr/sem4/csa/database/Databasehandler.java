package hwr.sem4.csa.database;

import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;

import javax.jdo.JDOHelper;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Databasehandler {
    //Singleton-Pattern
    static Databasehandler thisDBHandler = new Databasehandler();
    private EntityManagerFactory emFactory;

    private Databasehandler() {    }

    //Method to access Instance
    static public Databasehandler instanceOf() {
        return thisDBHandler;
    }

    //Needs to be called before accessing ODB
    public void initObjectDBConnection(){
        emFactory = Persistence.createEntityManagerFactory("objectdb:" +
                "//ec2-34-203-244-142.compute-1.amazonaws.com:6136/reviewB.odb;user=admin;password=admin");
     /*   emFactory = Persistence.createEntityManagerFactory("objectdb:" +
                "//localhost:6136/reviewB.odb;user=admin;password=admin");*/
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    //Closes connection
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

    //Inserts all objects part of a list into DB; no checks are made
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

    //Returns all Participators that are part of a specific community
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
        List<Participator> participatorList = typedResultQuery.setParameter("username",username).getResultList();
        if(participatorList.size() == 1){
            return participatorList.get(0);
        }else {
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

    //Update Participator
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

    //Update Community
    public void updateCommunity(String id, String name, String creationTime, ArrayList<Task> tasksList, ArrayList<Dotos> dotosList){
        removeCommunityById(id);
        Community c = new Community();
        c.setId(id);
        c.setName(name);
        c.setCreationTime(creationTime);
        c.setDotosList(dotosList);
        c.setTaskList(tasksList);
        insert(c);
    }

    //SysAdmin get all registered Participators
    public List<Participator> getAllParticipators(){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Participator> typedQuery = em.createQuery("SELECT p FROM Participator p",Participator.class);
        return typedQuery.getResultList();
    }

    //SysAdmin get all registered Communities
    public List<Community> getAllCommunities(){
        EntityManager em = emFactory.createEntityManager();
        TypedQuery<Community> typedQuery = em.createQuery("SELECT c FROM Community c",Community.class);
        return typedQuery.getResultList();
    }

    //Sysadmin remove Participator from registered Participators
    public void removeParticipatorByUsername(String username){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        Query currentQuery = em.createQuery("DELETE FROM Participator p WHERE p.username = :username", Participator.class);
        currentQuery.setParameter("username",username).executeUpdate();
        em.getTransaction().commit();
        em.close();

    }

    //Sysadmin remove Community from registered Communities
    public void removeCommunityById(String id){
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        Query currentQuery = em.createQuery("DELETE FROM Community c WHERE c.id = :id", Community.class);
        currentQuery.setParameter("id",id).executeUpdate();
        em.getTransaction().commit();
        em.close();
    }




}

