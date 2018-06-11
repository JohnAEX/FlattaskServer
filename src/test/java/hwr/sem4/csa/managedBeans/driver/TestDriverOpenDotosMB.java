package hwr.sem4.csa.managedBeans.driver;

import hwr.sem4.csa.managedBeans.steps.OpenDotosSteps;
import org.jbehave.core.junit.JUnitStories;

import java.util.Arrays;
import java.util.List;

public class TestDriverOpenDotosMB extends JUnitStories {

    public TestDriverOpenDotosMB()
    {
        super();
        this.configuredEmbedder().candidateSteps().add(new OpenDotosSteps());
    }

    @Override
    protected List<String> storyPaths()
    {
        return Arrays.asList("hwr.sem4.csa.managedBeans/openDotosStory.story");
    }

}
