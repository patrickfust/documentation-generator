package dk.fust.docgen.erdiagram;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.destination.Destination;
import dk.fust.docgen.erdiagram.generators.UMLGenerator;
import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * Configuration for generating entity-relation diagrams
 */
@Data
public class ERDiagramConfiguration implements GeneratorConfiguration {

    private UMLGenerator umlGenerator = UMLGenerator.MERMAID;

    private List<GenerateKey> generateKeys;

    private File documentationFile;

    private Destination destination;

    @Override
    public void validate() {
        Assert.isNotNull(documentationFile, "documentationFile must not be null");
        Assert.isTrue(documentationFile.exists(), "documentationFile must exist");
        Assert.isNotNull(umlGenerator, "umlGenerator must not be null");
        Assert.isNotNull(generateKeys, "generateKeys must not be null");
    }

    @Override
    public Generator getGenerator() {
        return new ERDiagramGenerator();
    }

}
