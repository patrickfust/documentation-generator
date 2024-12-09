package dk.fust.docgen.datadict

import dk.fust.docgen.Generator
import dk.fust.docgen.GeneratorConfiguration
import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MockDestination
import dk.fust.docgen.format.table.MockTableFormatter
import dk.fust.docgen.service.DocumentationConfigurationLoaderService
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
                exportExample: expExamples,
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
        addDescForFile | expFilename | expDescription | expColumn | expKeys | expType | expPosition | expExamples | cellSize | rowSize
        false          | true        | true           | true      | true    | true    | true        | true        | 8        | 4
        false          | false       | true           | true      | true    | true    | true        | true        | 7        | 4
        false          | false       | false          | true      | true    | true    | true        | true        | 6        | 4
        false          | false       | false          | false     | true    | true    | true        | true        | 5        | 4
        false          | false       | false          | false     | false   | true    | true        | true        | 4        | 4
        false          | false       | false          | false     | false   | false   | true        | true        | 3        | 4
        false          | false       | false          | false     | false   | false   | false       | true        | 2        | 4
        false          | false       | false          | false     | false   | false   | false       | false       | 1        | 4
        true           | true        | true           | true      | true    | true    | true        | true        | 8        | 6
        true           | false       | true           | true      | true    | true    | true        | true        | 7        | 6
        true           | false       | false          | true      | true    | true    | true        | true        | 6        | 6
        true           | false       | false          | false     | true    | true    | true        | true        | 5        | 6
        true           | false       | false          | false     | false   | true    | true        | true        | 4        | 6
        true           | false       | false          | false     | false   | false   | true        | true        | 3        | 6
        true           | false       | false          | false     | false   | false   | false       | true        | 2        | 6
        true           | false       | false          | false     | false   | false   | false       | false       | 1        | 6
    }

    def "read using generator configuration"() {
        given:
        DocumentationConfigurationLoaderService service = new DocumentationConfigurationLoaderService()

        when:
        List<GeneratorConfiguration> configurations = service.readConfigurations(TestHelper.getTestFile('generator-configuration.yml'))

        then:
        configurations[0].destination
        configurations[1].destination
    }

}
