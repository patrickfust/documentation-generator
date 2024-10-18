package dk.fust.docgen.erdiagram.generators;

/**
 * Factory to choose the correct generator from
 */
public class ERGeneratorFactory {

    /**
     * Instantiate the generator
     * @param umlGenerator the type of generator to use
     * @return an instance of the generator
     */
    public static ERGenerator getGenerator(UMLGenerator umlGenerator) {
        return switch (umlGenerator) {
            case MERMAID -> new MermaidGenerator();
            case PLANTUML -> new PlantUMLGenerator();
        };
    }

}
