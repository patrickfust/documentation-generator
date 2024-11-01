package dk.fust.docgen.model

import spock.lang.Specification
import spock.lang.Unroll

class DocumentationSpec extends Specification {

    @Unroll
    def "setting generate id on table to #generateId"() {
        given:
        Documentation documentation = new Documentation()
        documentation.tables = [new Table(name: 'table', generation: [generateId: generateId])]

        when:
        Table table = documentation.getTable('table')
        Generation generationForTable = documentation.getGenerationForTable(table)

        then:
        generationForTable.hasGenerateId()
        generationForTable.getGenerateId() == generateId

        where:
        generateId << [true, false]
    }
}
