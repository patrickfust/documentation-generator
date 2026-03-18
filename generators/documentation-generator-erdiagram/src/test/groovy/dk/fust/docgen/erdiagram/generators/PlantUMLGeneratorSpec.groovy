package dk.fust.docgen.erdiagram.generators

import dk.fust.docgen.GeneratorConfiguration
import dk.fust.docgen.TestHelper
import dk.fust.docgen.erdiagram.ERDiagramConfiguration
import dk.fust.docgen.model.Documentation
import spock.lang.Specification

class PlantUMLGeneratorSpec extends Specification {

    def "generate plantUML ER diagram"() {
        given:
        PlantUMLGenerator plantUMLGenerator = new PlantUMLGenerator()
        GeneratorConfiguration conf = new ERDiagramConfiguration()
        Documentation documentation = TestHelper.loadTestDocumentation('documentation-erdiagram.yaml')

        when:
        String uml = plantUMLGenerator.generateUML(null, documentation, conf)

        then:
        uml.contains 'table_a'
        uml.contains 'table_b'
        uml.contains 'xxx.table_b::field_b ||--o{ xxx.table_a::field_a'

        and: 'combined foreign keys are generated as expected'
        uml.contains '$fk("field_b_combined"): int'
        uml.contains 'xxx.table_b::field_b ||--o{ xxx.combined_foreign_key_table::field_b_combined'
    }

}
