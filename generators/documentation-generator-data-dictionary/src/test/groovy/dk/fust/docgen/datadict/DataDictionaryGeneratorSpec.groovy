package dk.fust.docgen.datadict

import dk.fust.docgen.Generator
import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MockDestination
import dk.fust.docgen.format.table.MockTableFormatter
import spock.lang.Specification

class DataDictionaryGeneratorSpec extends Specification {

    def "generate data dictionary"() {
        given:
        MockDestination mockDestination = new MockDestination()
        MockTableFormatter mockTableFormatter = new MockTableFormatter(mockFormat: 'X')
        DataDictionaryConfiguration configuration = new DataDictionaryConfiguration(
                destination: mockDestination,
                tableFormatter: mockTableFormatter,
                exportFilename: expFilename,
                exportDescription: expDescription,
                exportColumn: expColumn,
                exportKeys: expKeys,
                exportDataType: expType,
                exportPosition: expPosition,
                addDescriptionForFile: addDescForFile
        )

        Generator generator = configuration.getGenerator()

        when:
        generator.generate(TestHelper.loadTestDocumentation("documentation-data-dictionary.yaml"), configuration);

        then:
        mockDestination.document == 'X'
        mockTableFormatter.formatTableArgument.rows.size() == rowSize
        mockTableFormatter.formatTableArgument.rows[0].cells.size() == cellSize
        mockTableFormatter.formatTableArgument.rows[1].cells.size() == cellSize
        mockTableFormatter.formatTableArgument.rows[2].cells.size() == cellSize

        where:
        addDescForFile | expFilename | expDescription | expColumn | expKeys | expType | expPosition | cellSize | rowSize
        false          | true        | true           | true      | true    | true    | true        | 7        | 3
        false          | false       | true           | true      | true    | true    | true        | 6        | 3
        false          | false       | false          | true      | true    | true    | true        | 5        | 3
        false          | false       | false          | false     | true    | true    | true        | 4        | 3
        false          | false       | false          | false     | false   | true    | true        | 3        | 3
        false          | false       | false          | false     | false   | false   | true        | 2        | 3
        false          | false       | false          | false     | false   | false   | false       | 1        | 3
        true           | true        | true           | true      | true    | true    | true        | 7        | 4
        true           | false       | true           | true      | true    | true    | true        | 6        | 4
        true           | false       | false          | true      | true    | true    | true        | 5        | 4
        true           | false       | false          | false     | true    | true    | true        | 4        | 4
        true           | false       | false          | false     | false   | true    | true        | 3        | 4
        true           | false       | false          | false     | false   | false   | true        | 2        | 4
        true           | false       | false          | false     | false   | false   | false       | 1        | 4
    }

}
