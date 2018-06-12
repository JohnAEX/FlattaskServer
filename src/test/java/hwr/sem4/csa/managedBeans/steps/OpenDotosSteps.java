package hwr.sem4.csa.managedBeans.steps;

import hwr.sem4.csa.database.Databasehandler;
import hwr.sem4.csa.managedBeans.OpenDotosManagedBean;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Parameters;
import org.jbehave.core.steps.Steps;
import org.primefaces.model.DualListModel;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


public class OpenDotosSteps extends Steps {
    private OpenDotosManagedBean testBean;

    private Databasehandler localHandler = Databasehandler.instanceOf();
    private List<Participator> localParticipators;
    private Community localPreconditionCommunity;
    private Community localPostconditionCommunity;

    @Given("there exists a Community as follows: $comTable")
    public void initCom(ExamplesTable comTable)
    {
        this.localPreconditionCommunity = new Community();
        for(Parameters row : comTable.getRowsAsParameters()) {
            String name = row.valueAs("name", String.class);
            String creationTime = row.valueAs("creationTime", String.class);
            String id = row.valueAs("id", String.class);
            this.localPreconditionCommunity.setName(name);
            this.localPreconditionCommunity.setCreationTime(creationTime);
            this.localPreconditionCommunity.setId(id);
        }
    }
    @Given("there exist Participators as follows: $particTable")
    public void initPartics(ExamplesTable particTable)
    {
        this.localParticipators = new ArrayList<>();
        for(Parameters row : particTable.getRowsAsParameters()) {
            String username = row.valueAs("username", String.class);
            String password = row.valueAs("password", String.class);
            String firstname = row.valueAs("firstname", String.class);
            String lastname = row.valueAs("lastname", String.class);
            int balance = row.valueAs("balance", int.class);
            String role = row.valueAs("role", String.class);
            String communityId = this.localPreconditionCommunity.getId();

            Participator newPartic = new Participator(username, password, firstname, lastname, balance, role, communityId);

            this.localParticipators.add(newPartic);
        }
    }
    @Given("the Tasks in that Community are as follows: $taskTable")
    public void initTasks(ExamplesTable taskTable)
    {
        this.localPreconditionCommunity.setTaskList(new ArrayList<>());
        for(Parameters row : taskTable.getRowsAsParameters()) {
            String id = String.valueOf(System.nanoTime());
            String title = row.valueAs("title", String.class);
            String description = row.valueAs("description", String.class);
            int baseValue = row.valueAs("baseValue", int.class);
            int baseDuration = row.valueAs("baseDuration", int.class);

            Task newTask = new Task(title, description, baseValue, baseDuration);
            newTask.setId(id);

            this.localPreconditionCommunity.getTaskList().add(newTask);
        }
    }
    @Given("the Dotos in that Community are as follows: $dotosTable")
    public void initDotos(ExamplesTable dotosTable)
    {
        this.localPreconditionCommunity.setDotosList(new ArrayList<>());
        for(Parameters row : dotosTable.getRowsAsParameters()) {
            int id = (int) System.nanoTime();
            String title = row.valueAs("title", String.class);
            String description = row.valueAs("description", String.class);
            int value = row.valueAs("value", int.class);
            int duration = row.valueAs("duration", int.class);
            String assignedTo = row.valueAs("assignedTo", String.class);
            String assignedBy = row.valueAs("assignedBy", String.class);

            Dotos newDoto = new Dotos(title, description, value, duration, assignedTo, assignedBy);
            newDoto.setId(id);

            this.localPreconditionCommunity.getDotosList().add(newDoto);
        }
    }
    @When("the premise is persisted.")
    public void persistPremise()
    {
        this.localHandler.initObjectDBConnection();
        if(this.localHandler.getCommunityById(this.localPreconditionCommunity.getId()) == null) {
            this.localHandler.insert(this.localPreconditionCommunity);
            System.out.println("Community not present, inserting...");
        } else {
            this.localHandler.updateCommunity(this.localPreconditionCommunity.getId(), this.localPreconditionCommunity.getName(),
                    this.localPreconditionCommunity.getCreationTime(), this.localPreconditionCommunity.getTaskList(),
                    this.localPreconditionCommunity.getDotosList());
            System.out.println("Community present, overwriting...");
        }

        this.localHandler.insertList(this.localParticipators);
        this.localHandler.close();
    }


    @When("an OpenDotosManagedBean-Instance is generated for the user $username.")
    public void initManagedBean(String username)
    {
        List<Participator> subjects = this.localParticipators.stream()
                .filter(aPartic -> aPartic.getUsername().equals(username))
                .collect(Collectors.toList());

        if(subjects.size() != 1) {
            String errorMessage = String.format("Ambiguous usernames: %s", subjects.toString());
            throw new IllegalStateException(errorMessage);
        }

        this.testBean = new OpenDotosManagedBean();
        this.testBean.setLocalCommunity(this.localPreconditionCommunity);
        this.testBean.setLocalParticipator(subjects.get(0));
        this.testBean.setDotos(new DualListModel<>(new ArrayList<>(), new ArrayList<>()));

        this.splitDotosByAssignment(this.testBean.getLocalCommunity().getDotosList(),
                this.testBean.getDotos());
    }
    @When("$username grabs all open Dotos.")
    public void grabAllOpenDotosFor(String username)
    {
        this.testBean.getDotos().getTarget()
                .addAll(new ArrayList<>(this.testBean.getDotos().getSource()));
        this.testBean.getDotos().getSource().clear();
    }
    @When("all Dotos are unassigned.")
    public void unassignAllDotos()
    {
        System.out.println(this.testBean);
        this.testBean.getDotos().getSource()
                .addAll(new ArrayList<>(this.testBean.getDotos().getTarget()));
        this.testBean.getDotos().getTarget().clear();
    }


    @When("the performed action(s) are confirmed.")
    public void confirmActions()
    {
        this.testBean.confirmAssignment();
    }
    @Then("all Dotos should be assigned to $username.")
    public void checkDotoAssignment(String username)
    {
        if(username.equals("noone")) {
            username = null;
        }

        this.localHandler.initObjectDBConnection();
        this.localPostconditionCommunity = this.localHandler.getCommunityById(this.localPreconditionCommunity.getId());

        // Community existence
        assertThat(this.localPostconditionCommunity, is(notNullValue()));

        // DotosList existence
        assertThat(this.localPostconditionCommunity.getDotosList(), is(notNullValue()));

        // Length
        assertThat(this.localPostconditionCommunity.getDotosList().size(), is(this.localPreconditionCommunity.getDotosList().size()));

        // Assignment
        for(int i = 0; i < this.localPostconditionCommunity.getDotosList().size(); ++i) {
            Dotos actualDoto = this.localPostconditionCommunity.getDotosList().get(i);

            assertThat(actualDoto.getAssignedTo(), is(username));
        }

        // Still Assignment
        if(username != null) {
            // Assignment
            assertThat(this.testBean.getDotos().getSource().size(), is(0));
            assertThat(this.testBean.getDotos().getTarget().size(), is(this.localPreconditionCommunity.getDotosList().size()));
        } else {
            // Unassignment
            assertThat(this.testBean.getDotos().getSource().size(), is(this.localPreconditionCommunity.getDotosList().size()));
            assertThat(this.testBean.getDotos().getTarget().size(), is(0));

            final String usernameReference = username;
            this.testBean.getDotos().getTarget()
                    .forEach(aDoto -> assertThat(aDoto.getAssignedTo(), is(usernameReference)));
        }

        System.out.println(String.format("Check for assignment to %s has been successful", username));
    }

    /*
     * Assisting methods
     */

    private void splitDotosByAssignment(List<Dotos> dotoSource, DualListModel<Dotos> dualDotos)
    {
        Optional<List<Dotos>> sourceOptional = Optional.of(dotoSource);
        if(!sourceOptional.isPresent()) {
            return;
        }

        for(Iterator<Dotos> dotosIterator = dotoSource.iterator(); dotosIterator.hasNext(); ) {
            Dotos aDoto = dotosIterator.next();

            if(aDoto.getAssignedTo() == null || aDoto.getAssignedTo().equals("")) {
                // is open
                dualDotos.getSource().add(aDoto);
            } else {
                // is assigned
                dualDotos.getTarget().add(aDoto);
            }
        }
    }
}
