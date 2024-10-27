package dk.fust.docgen.maven;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DocumentationGeneratorMojoTest {
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void testLoadingMojo() throws Exception {
        File pom = new File("target/test-classes/project-to-test/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        DocumentationGeneratorMojo myMojo = (DocumentationGeneratorMojo) rule.lookupConfiguredMojo(pom, "generateDocumentation");
        assertNotNull(myMojo);
        myMojo.execute();
    }

    /**
     * Do not need the MojoRule.
     */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
        assertTrue(true);
    }

}

