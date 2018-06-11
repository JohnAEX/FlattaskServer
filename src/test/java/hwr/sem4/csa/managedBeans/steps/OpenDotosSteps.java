package hwr.sem4.csa.managedBeans.steps;

import hwr.sem4.csa.managedBeans.OpenDotosManagedBean;
import hwr.sem4.csa.util.Community;
import hwr.sem4.csa.util.Dotos;
import hwr.sem4.csa.util.Participator;
import hwr.sem4.csa.util.Task;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Parameters;
import org.jbehave.core.steps.Steps;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public class OpenDotosSteps extends Steps {
    private OpenDotosManagedBean testBean;

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
            String id = String.valueOf(System.nanoTime());
            this.localPreconditionCommunity.setName(name);
            this.localPreconditionCommunity.setCreationTime(creationTime);
            this.localPreconditionCommunity.setId(id);
        }
        throw new IllegalStateException("hiho");
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
            int id = Integer.parseInt(String.valueOf(System.nanoTime()));
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

    @When("an OpenDotosManagedBean-Instance is generated for the user $username")
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

        this.splitDotosByAssignment(this.testBean.getLocalCommunity().getDotosList(),
                this.testBean.getDotos().getSource(), this.testBean.getDotos().getTarget());
    }
    @When("$username grabs all open Dotos.")
    public void grabAllOpenDotosFor(String username)
    {
        this.testBean.getDotos().getTarget()
                .addAll(this.testBean.getDotos().getSource());
        this.testBean.getDotos().getSource().clear();
    }
    @When("the performed action(s) are confirmed.")
    public void confirmActions()
    {
        this.testBean.confirmAssignment();
        throw new IllegalStateException("Hammer!");
    }


    /*
     * Assisting methods
     */

    private void splitDotosByAssignment(List<Dotos> dotoSource, List<Dotos> openDotos, List<Dotos> assignedDotos)
    {
        Optional<List<Dotos>> sourceOptional = Optional.of(dotoSource);
        if(!sourceOptional.isPresent()) {
            return;
        }
        Optional<List<Dotos>> openOptional = Optional.of(openDotos);
        if(!openOptional.isPresent()) {
            openDotos = new ArrayList<>();
        }
        Optional<List<Dotos>> assignedOptional = Optional.of(assignedDotos);
        if(!assignedOptional.isPresent()) {
            assignedDotos = new ArrayList<>();
        }

        for(Iterator<Dotos> dotosIterator = dotoSource.iterator(); dotosIterator.hasNext(); ) {
            Dotos aDoto = dotosIterator.next();

            if(aDoto.getAssignedTo() == null || aDoto.getAssignedTo().equals("")) {
                // is open
                openDotos.add(aDoto);
            } else {
                // is assigned
                assignedDotos.add(aDoto);
            }
        }
    }
}
