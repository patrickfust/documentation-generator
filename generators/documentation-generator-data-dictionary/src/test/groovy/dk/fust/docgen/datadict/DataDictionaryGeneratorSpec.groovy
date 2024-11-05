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
                tableFormatter: mockTableFormatter
        )

        Generator generator = configuration.getGenerator()

        when:
        generator.generate(TestHelper.loadTestDocumentation("documentation-data-dictionary.yaml"), configuration);

        then:
        mockDestination.document == 'X'
        mockTableFormatter.formatTableArgument.rows.size() == 3
    }

}
