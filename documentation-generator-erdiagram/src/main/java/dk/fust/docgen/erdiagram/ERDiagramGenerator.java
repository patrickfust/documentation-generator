package dk.fust.docgen.erdiagram;

import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.Generator;
import dk.fust.docgen.erdiagram.generators.ERGenerator;
import dk.fust.docgen.erdiagram.generators.ERGeneratorFactory;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Generator for entity-relation diagrams
 */
@Slf4j
public class ERDiagramGenerator implements Generator {
    @Override
    public void generate(Documentation documentation, GeneratorConfiguration generatorConfiguration) throws IOException {
        ERDiagramConfiguration erDiagramConfiguration = (ERDiagramConfiguration) generatorConfiguration;
        if (erDiagramConfiguration != null) {
            log.info("Generating ERDiagram...");
            Assert.isNotNull(erDiagramConfiguration.getDestination(), "destination must not be null");
            erDiagramConfiguration.getDestination().validate();
            ERGenerator generator = ERGeneratorFactory.getGenerator(erDiagramConfiguration.getUmlGenerator());
            for (GenerateKey generateKey : erDiagramConfiguration.getGenerateKeys()) {
                String uml = generator.generateUML(generateKey.getFilter(), documentation, generatorConfiguration);
                String document = """
```%s
%s
```
""".formatted(generator.getMarkdownType(), uml);
                erDiagramConfiguration.getDestination().sendDocumentToDestination(document, generateKey.getDestinationKey());
            }
        }

    }
}
