package hwr.sem4.csa.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyCreation {

    public static DummyCreation dummy = new DummyCreation();

    private DummyCreation(){

    }

    public static DummyCreation instanceOf() {
        return dummy;
    }

    public List<Participator> createDummyParticipators(int amount){
        List<Participator> participatorList = new ArrayList();
        for(int n = 0; n < amount; n++){
            Participator p = new Participator();
            p.setUsername(getRandomString(10));
            p.setPassword(getRandomString(4));
            p.setFirstName(getRandomString(4));
            p.setLastName(getRandomString(4));
            p.setRole("user");
            p.setBalance(100);
            p.setCommunityId(getRandomString(6));
            p.setCreationTime("Sometime");
            p.setHistory("Created");
            participatorList.add(p);
        }
        return participatorList;
    }

    public List<Community> createDummyCommunities(int amount){
        List<Community> communityList = new ArrayList<Community>();
        for(int n = 0; n < amount; n++){
            Community c = new Community();
            c.setId(getRandomString(6));
            c.setName(getRandomString(10));
            c.setCreationTime("Sometime");
            c.setTaskList(null);
            c.setDotosList(null);
            communityList.add(c);
        }
        return communityList;
    }

    public List<List> createUsableData(int amount){
        List<Participator> participatorList;
        List<Community> communityList;

        participatorList = createDummyParticipators(amount);
        communityList = createDummyCommunities(1);
        for(Participator p : participatorList){
            p.setCommunityId(communityList.get(0).getId());
        }

        List<List> returnList = new ArrayList<>();
        returnList.add(participatorList);
        returnList.add(communityList);
        return returnList;
    }

    public String getRandomString(int length){
       return RandomStringUtils.randomAlphabetic(length);
    }
}
