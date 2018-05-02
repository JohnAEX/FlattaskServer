package hwr.sem4.csa.managedBeans;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class SysAdminManagedBean {
    private List<Participator> participatorList;
    private List<Community> communityList;

    //New Community Vars
    private String cId;
    private String cName;
    private List<Dotos> cDotoList;
    private List<Task> cTaskList;
    private String cCreationTime;

    //New Participator Vars
    private String pUsername;
    private String pPassword;
    private String pFirstname;
    private String pLastname;
    private int pBalance;
    private String pCreationTime;
    private String pCommunityID;
    private String pRole;

    //Selection
    private Participator selectedParticipator;
    private Community selectedCommunity;

    //Generation
    private int amountToGenerate;

    public int getAmountToGenerate() {
        return amountToGenerate;
    }

    public void setAmountToGenerate(int amountToGenerate) {
        this.amountToGenerate = amountToGenerate;
    }

    @PostConstruct
    public void init(){
        Databasehandler.instanceOf().initObjectDBConnection();
        participatorList = Databasehandler.instanceOf().getAllParticipators();
        communityList = Databasehandler.instanceOf().getAllCommunities();
        Databasehandler.instanceOf().close();
    }

    public void updatePList(){
        Databasehandler.instanceOf().initObjectDBConnection();
        participatorList = Databasehandler.instanceOf().getAllParticipators();
        Databasehandler.instanceOf().close();
    }

    public void updateCList(){
        Databasehandler.instanceOf().initObjectDBConnection();
        communityList = Databasehandler.instanceOf().getAllCommunities();
        Databasehandler.instanceOf().close();
    }

    public void removeParticipator(){
        System.out.println("Trying to remove Participator: " + selectedParticipator.getUsername());
        Databasehandler.instanceOf().initObjectDBConnection();
        Databasehandler.instanceOf().removeParticipatorByUsername(selectedParticipator.getUsername());
        Databasehandler.instanceOf().close();
        updatePList();
    }

    public void removeCommunity(){
        Databasehandler.instanceOf().initObjectDBConnection();
        Databasehandler.instanceOf().removeCommunityById(selectedCommunity.getId());
        Databasehandler.instanceOf().close();
        updateCList();
    }

    public void generateValidData(){
        List<List> returnList = DummyCreation.instanceOf().createUsableData(this.amountToGenerate);
        Databasehandler.instanceOf().initObjectDBConnection();
        Databasehandler.instanceOf().insertList(returnList.get(0));
        Databasehandler.instanceOf().insert(returnList.get(1).get(0));
        Databasehandler.instanceOf().close();
    }

    public void addParticipator(){
        System.out.println("Called addParticipator");
        Participator p = new Participator();
        p.setUsername(this.pUsername);
        p.setPassword(this.pPassword);
        p.setFirstName(this.pFirstname);
        p.setLastName(this.pLastname);
        p.setRole(this.pRole);
        p.setBalance(this.pBalance);
        p.setCommunityId(this.pCommunityID);
        p.setCreationTime(this.pCreationTime);
        p.setHistory("Created");

        Databasehandler.instanceOf().initObjectDBConnection();
        Databasehandler.instanceOf().insert(p);
        Databasehandler.instanceOf().close();
    }

    public void addCommunity(){
        System.out.println("Called addCommunity");
        Community c = new Community();
        c.setId(this.cId);
        c.setName(this.cName);
        c.setCreationTime(this.cCreationTime);

        Databasehandler.instanceOf().initObjectDBConnection();
        Databasehandler.instanceOf().insert(c);
        Databasehandler.instanceOf().close();
    }

    public void onCellEditParticipator(Participator uP) {

            Databasehandler.instanceOf().initObjectDBConnection();
            Databasehandler.instanceOf().updateParticipator(uP.getUsername(), uP.getPassword(), uP.getFirstName(),
                    uP.getLastName(), uP.getBalance(),uP.getRole(), uP.getCommunityId(), uP.getCreationTime());
            Databasehandler.instanceOf().close();

    }

    public void onCellEditCommunity(Community uC) {

            Databasehandler.instanceOf().initObjectDBConnection();
            Databasehandler.instanceOf().updateCommunity(uC.getId(),uC.getName(), uC.getCreationTime(), uC.getTaskList(), uC.getDotosList());
            Databasehandler.instanceOf().close();

    }

    public List<Participator> getParticipatorList() {
        return participatorList;
    }

    public void setParticipatorList(List<Participator> participatorList) {
        this.participatorList = participatorList;
    }

    public List<Community> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<Community> communityList) {
        this.communityList = communityList;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public List<Dotos> getcDotoList() {
        return cDotoList;
    }

    public void setcDotoList(List<Dotos> cDotoList) {
        this.cDotoList = cDotoList;
    }

    public List<Task> getcTaskList() {
        return cTaskList;
    }

    public void setcTaskList(List<Task> cTaskList) {
        this.cTaskList = cTaskList;
    }

    public String getcCreationTime() {
        return cCreationTime;
    }

    public void setcCreationTime(String cCreationTime) {
        this.cCreationTime = cCreationTime;
    }

    public String getpUsername() {
        return pUsername;
    }

    public void setpUsername(String pUsername) {
        this.pUsername = pUsername;
    }

    public String getpPassword() {
        return pPassword;
    }

    public void setpPassword(String pPassword) {
        this.pPassword = pPassword;
    }

    public String getpFirstname() {
        return pFirstname;
    }

    public void setpFirstname(String pFirstname) {
        this.pFirstname = pFirstname;
    }

    public String getpLastname() {
        return pLastname;
    }

    public void setpLastname(String pLastname) {
        this.pLastname = pLastname;
    }

    public int getpBalance() {
        return pBalance;
    }

    public void setpBalance(int pBalance) {
        this.pBalance = pBalance;
    }

    public String getpCreationTime() {
        return pCreationTime;
    }

    public void setpCreationTime(String pCreationTime) {
        this.pCreationTime = pCreationTime;
    }

    public String getpCommunityID() {
        return pCommunityID;
    }

    public void setpCommunityID(String pCommunityID) {
        this.pCommunityID = pCommunityID;
    }

    public String getpRole() {
        return pRole;
    }

    public void setpRole(String pRole) {
        this.pRole = pRole;
    }

    public Participator getSelectedParticipator() {
        return selectedParticipator;
    }

    public void setSelectedParticipator(Participator selectedParticipator) {
        this.selectedParticipator = selectedParticipator;
    }

    public Community getSelectedCommunity() {
        return selectedCommunity;
    }

    public void setSelectedCommunity(Community selectedCommunity) {
        this.selectedCommunity = selectedCommunity;
    }
}
