package hwr.sem4.csa.managedBeans.driver;

import hwr.sem4.csa.managedBeans.steps.OpenDotosSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Arrays;
import java.util.List;

public class TestDriverOpenDotosMB extends JUnitStories {

    public TestDriverOpenDotosMB()
    {
        super();
        this.configuredEmbedder().candidateSteps().add(new OpenDotosSteps());
    }

    @Override
    public InjectableStepsFactory stepsFactory()
    {
        return new InstanceStepsFactory(this.configuration(), new OpenDotosSteps());
    }

    @Override
    public Configuration configuration()
    {
        return new MostUsefulConfiguration();
    }

    @Override
    protected List<String> storyPaths()
    {
        return Arrays.asList("hwr.sem4.csa.managedBeans/openDotosStory.story");
    }

}
