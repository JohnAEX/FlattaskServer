package hwr.sem4.csa.managedBeans.steps;

import hwr.sem4.csa.comparators.DotosComparator;
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

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThan;


public class OpenDotosSteps extends Steps {
    private final PrintStream OUT = System.out;
    private OpenDotosManagedBean testBean;
    private long timer;
    private Databasehandler localHandler = Databasehandler.instanceOf();

    private List<Participator> localParticipators;
    private Participator activeParticipator;
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
        OUT.println(String.format("Community %s has been initialized", this.localPreconditionCommunity.getId()));
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
        OUT.println(String.format("Participators %s have been initialized", this.localParticipators.toString()));
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
        OUT.println(String.format("The Tasks %s have been initialized", this.localPreconditionCommunity.getTaskList().toString()));
    }
    @Given("the Dotos in that Community are as follows: $dotosTable")
    public void initDotos(ExamplesTable dotosTable)
    {
        ArrayList<Dotos> allDotos = new ArrayList<>(this.readDotosFrom(dotosTable));

        this.localPreconditionCommunity.setDotosList(allDotos);
        OUT.println(String.format("Dotos %s have been initialized", this.localPreconditionCommunity.getDotosList().toString()));
    }
    @When("the premise is persisted.")
    public void persistPremise()
    {
        this.timer = System.nanoTime();
        this.localHandler.initObjectDBConnection();
        if(this.localHandler.getCommunityById(this.localPreconditionCommunity.getId()) == null) {
            OUT.println("Community not present, inserting...");
            this.localHandler.insert(this.localPreconditionCommunity);
        } else {
            OUT.println("Community present, overwriting...");
            this.localHandler.updateCommunity(this.localPreconditionCommunity.getId(), this.localPreconditionCommunity.getName(),
                    this.localPreconditionCommunity.getCreationTime(), this.localPreconditionCommunity.getTaskList(),
                    this.localPreconditionCommunity.getDotosList());
        }

        for(Participator aParticipator : this.localParticipators) {
            if(this.localHandler.getParticipatorByUsername(aParticipator.getUsername()) == null) {
                // Participator is new
                this.localHandler.insert(aParticipator);
                OUT.println(String.format("New Participator %s has been created.", aParticipator.getUsername()));
            } else {
                // Participator is already present
                this.localHandler.updateParticipator(
                        aParticipator.getUsername(),
                        aParticipator.getPassword(),
                        aParticipator.getFirstName(),
                        aParticipator.getLastName(),
                        aParticipator.getBalance(),
                        aParticipator.getRole(),
                        aParticipator.getCommunityId(),
                        aParticipator.getCreationTime()
                );
                OUT.println(String.format("Participator %s has been overwritten.", aParticipator.getUsername()));
            }
        }
        this.localHandler.close();
        this.timer = System.nanoTime() - this.timer;
    }


    @When("an OpenDotosManagedBean-Instance is generated for the user $username.")
    public void initManagedBean(String username)
    {
        List<Participator> subjects = this.localParticipators.stream()
                .filter(aPartic -> aPartic.getUsername().equals(username))
                .collect(Collectors.toList());

        if(subjects.size() != 1) {
            String errorMessage = String.format("Ambiguous usernames: %s",
                    subjects.toString());
            throw new IllegalStateException(errorMessage);
        }
        this.activeParticipator = subjects.get(0);

        this.timer = System.nanoTime();
        this.testBean = new OpenDotosManagedBean();
        this.testBean.setLocalCommunity(this.localPreconditionCommunity);
        this.testBean.setLocalParticipator(this.activeParticipator);
        this.testBean.setDotos(new DualListModel<>(new ArrayList<>(), new ArrayList<>()));

        this.splitDotosByAssignment(this.testBean.getLocalCommunity().getDotosList(),
                this.testBean.getDotos());
        this.timer = System.nanoTime() - this.timer;
        OUT.println(String.format("OpenDotosManagedBean %s has been initialized",
                this.testBean.toString()));
    }
    @When("The Paticipator grabs all open Dotos.")
    public void grabAllOpenDotosFor()
    {
        this.timer = System.nanoTime();
        this.testBean.getDotos().getTarget()
                .addAll(new ArrayList<>(this.testBean.getDotos().getSource()));
        this.testBean.getDotos().getSource().clear();
        this.timer = System.nanoTime() - this.timer;
        OUT.println(String.format("The Dotos %s have been grabbed for OpenDotosManagedBean %s.",
                this.testBean.getDotos().getTarget().toString(), this.testBean.toString()));
    }
    @When("all Dotos are unassigned.")
    public void unassignAllDotos()
    {
        this.timer = System.nanoTime();
        this.testBean.getDotos().getSource()
                .addAll(new ArrayList<>(this.testBean.getDotos().getTarget()));
        this.testBean.getDotos().getTarget().clear();
        this.timer = System.nanoTime() - this.timer;
        OUT.println(String.format("All Dotos have been unassigned for OpenDotosManagedBean %s", this.testBean.toString()));
    }

    @When("the performed action(s) are confirmed.")
    public void confirmActions()
    {
        this.timer = System.nanoTime();
        this.testBean.confirmAssignment();
        this.timer = System.nanoTime() - this.timer;
        OUT.println(String.format("Confirm has been executed for OpenDotosManagedBean %s",
                this.testBean.toString()));
    }


    @Then("the OpenDotosManagedBean should be owned by $username.")
    public void checkParticInMB(String username)
    {
        assertThat(username, is(this.activeParticipator.getUsername()));
    }
    @Then("the OpenDotosManagedBean should refer to the Community $communityId.")
    public void checkCommInMB(String communityId)
    {
        assertThat(communityId, is(this.localPreconditionCommunity.getId()));
    }
    @Then("the OpenDotosManagedBean should have source Dotos as follows: $sourceDotosTable")
    public void checkSourceDotosInMB(ExamplesTable sourceDotosTable)
    {
        List<Dotos> expectedSourceDotos = this.readDotosFrom(sourceDotosTable);
        List<Dotos> actualSourceDotos = this.testBean.getDotos().getSource();

        expectedSourceDotos.sort(new DotosComparator());
        actualSourceDotos.sort(new DotosComparator());

        assertThat(actualSourceDotos, is(expectedSourceDotos));
    }
    @Then("the OpenDotosManagedBean should have target Dotos as follows: $targetDotosTable")
    public void checkTargetDotosInMB(ExamplesTable targetDotosTable)
    {
        List<Dotos> expectedTargetDotos = this.readDotosFrom(targetDotosTable);
        List<Dotos> actualTargetDotos = this.testBean.getDotos().getTarget();

        expectedTargetDotos.sort(new DotosComparator());
        actualTargetDotos.sort(new DotosComparator());

        assertThat(expectedTargetDotos, is(actualTargetDotos));
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

        OUT.println(String.format("Check for assignment to %s has been successful", username));
    }

    @Then("the execution time for the last When-Statement should be less than $timeLimit milliseconds.")
    public void checkTimer(long lowerLimit)
    {
        long actualResponseTime = this.timer/1000000;
        assertThat(actualResponseTime, is(lessThan(lowerLimit)));
        OUT.println(String.format("Time limit was not exceeded, expected: %d, was: %d",
                lowerLimit, actualResponseTime));
    }

    /*
     * Assisting methods
     */

    private List<Dotos> readDotosFrom(ExamplesTable dotosTable)
    {
        List<Dotos> dotosList = new ArrayList<>();

        for(int i = 0; i < dotosTable.getRowCount(); ++i) {
            Parameters row = dotosTable.getRowAsParameters(i);
            String title = row.valueAs("title", String.class);
            String description = row.valueAs("description", String.class);
            int value = row.valueAs("value", int.class);
            int duration = row.valueAs("duration", int.class);
            String assignedTo = row.valueAs("assignedTo", String.class);
            String assignedBy = row.valueAs("assignedBy", String.class);

            Dotos newDoto = new Dotos(title, description, value, duration, assignedTo, assignedBy);
            newDoto.setId(i);
            dotosList.add(newDoto);
        }

        return dotosList;

    }
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
