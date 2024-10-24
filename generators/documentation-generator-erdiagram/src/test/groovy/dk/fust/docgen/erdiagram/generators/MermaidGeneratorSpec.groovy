package dk.fust.docgen.erdiagram.generators

import dk.fust.docgen.GeneratorConfiguration
import dk.fust.docgen.TestHelper
import dk.fust.docgen.erdiagram.ERDiagramConfiguration
import dk.fust.docgen.model.Documentation
import spock.lang.Specification

class MermaidGeneratorSpec extends Specification {

    def "generate mermaid ER diagram"() {
        given:
        MermaidGenerator mermaidGenerator = new MermaidGenerator()
        GeneratorConfiguration conf = new ERDiagramConfiguration()
        Documentation documentation = TestHelper.loadTestDocumentation('documentation-erdiagram.yaml')

        when:
        String uml = mermaidGenerator.generateUML(null, documentation, conf)

        then:
        uml.contains 'table_a'
        uml.contains 'table_b'
    }
}
