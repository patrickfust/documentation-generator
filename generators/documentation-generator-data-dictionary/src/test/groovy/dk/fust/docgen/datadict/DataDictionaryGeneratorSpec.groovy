package dk.fust.docgen.datadict

import dk.fust.docgen.Generator
import dk.fust.docgen.GeneratorConfiguration
import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MockDestination
import dk.fust.docgen.format.table.Alignment
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
                addDescriptionForFile: addDescFile,
                schemaName: schemaName
        )
        configuration.columnFilename.export = expFile
        configuration.columnSchema.export = expSchema
        configuration.columnDescription.export = expDesc
        configuration.columnColumn.export = expColumn
        configuration.columnKeys.export = expKeys
        configuration.columnType.export = expType
        configuration.columnPosition.export = expPosition
        configuration.columnExample.export = expExam

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
        addDescFile | expFile | expDesc | expColumn | expKeys | expType | expPosition | expExam | expSchema | schemaName | cellSize | rowSize
        false       | true    | true    | true      | true    | true    | true        | true    | true      | 'schema'   | 9        | 4
        false       | true    | true    | true      | true    | true    | true        | true    | false     | null       | 8        | 4
        false       | false   | true    | true      | true    | true    | true        | true    | false     | null       | 7        | 4
        false       | false   | false   | true      | true    | true    | true        | true    | false     | null       | 6        | 4
        false       | false   | false   | false     | true    | true    | true        | true    | false     | null       | 5        | 4
        false       | false   | false   | false     | false   | true    | true        | true    | false     | null       | 4        | 4
        false       | false   | false   | false     | false   | false   | true        | true    | false     | null       | 3        | 4
        false       | false   | false   | false     | false   | false   | false       | true    | false     | null       | 2        | 4
        false       | false   | false   | false     | false   | false   | false       | false   | false     | null       | 1        | 4
        true        | true    | true    | true      | true    | true    | true        | true    | true      | 'schema'   | 9        | 6
        true        | true    | true    | true      | true    | true    | true        | true    | false     | null       | 8        | 6
        true        | false   | true    | true      | true    | true    | true        | true    | false     | null       | 7        | 6
        true        | false   | false   | true      | true    | true    | true        | true    | false     | null       | 6        | 6
        true        | false   | false   | false     | true    | true    | true        | true    | false     | null       | 5        | 6
        true        | false   | false   | false     | false   | true    | true        | true    | false     | null       | 4        | 6
        true        | false   | false   | false     | false   | false   | true        | true    | false     | null       | 3        | 6
        true        | false   | false   | false     | false   | false   | false       | true    | false     | null       | 2        | 6
        true        | false   | false   | false     | false   | false   | false       | false   | false     | null       | 1        | 6
    }

    def "read using generator configuration"() {
        given:
        DocumentationConfigurationLoaderService service = new DocumentationConfigurationLoaderService()

        when:
        List<GeneratorConfiguration> configurations = service.readConfigurations(TestHelper.getTestFile('generator-configuration.yml'))
        DataDictionaryConfiguration firstDataDictionaryConfiguration = configurations[0] as DataDictionaryConfiguration

        then:
        configurations[0].destination
        configurations[1].destination

        and: 'columns are read'
        firstDataDictionaryConfiguration.columnTable.export
        firstDataDictionaryConfiguration.columnTable.header == "My header"
        !firstDataDictionaryConfiguration.columnMandatory.export
        firstDataDictionaryConfiguration.columnDescription.alignment == Alignment.RIGHT

        and: 'defaults has not changed'
        firstDataDictionaryConfiguration.columnMandatory.header == "Mandatory"
        firstDataDictionaryConfiguration.columnExample.header == "Example"
        firstDataDictionaryConfiguration.columnDescription.export
    }

}
