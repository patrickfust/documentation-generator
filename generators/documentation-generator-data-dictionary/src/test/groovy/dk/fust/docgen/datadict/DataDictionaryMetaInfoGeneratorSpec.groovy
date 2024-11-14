package dk.fust.docgen.datadict

import dk.fust.docgen.Generator
import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MockDestination
import dk.fust.docgen.format.table.JsonTableFormatter
import dk.fust.docgen.format.table.TableFormatter
import spock.lang.Specification

class DataDictionaryMetaInfoGeneratorSpec extends Specification {

    def "generate data dictionary meta info as json"() {
        given:
        MockDestination mockDestination = new MockDestination()
        TableFormatter tableFormatter = new JsonTableFormatter()
        DataDictionaryMetaInfoConfiguration configuration = new DataDictionaryMetaInfoConfiguration(
                destination: mockDestination,
                tableFormatter: tableFormatter
        )

        Generator generator = configuration.getGenerator()

        when:
        generator.generate(TestHelper.loadTestDocumentation("documentation-data-dictionary.yaml"), configuration);

        then:
        mockDestination.document == """ \
        |[ {
        |  "Version" : "1.0.0",
        |  "Filename" : "some_file.csv"
        |}, {
        |  "Description" : "description of my other file",
        |  "Version" : "1.2.3",
        |  "Filename" : "some_other_file.csv"
        |} ]""".stripMargin().stripIndent()
    }

}
