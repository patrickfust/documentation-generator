package dk.fust.docgen.service

import com.fasterxml.jackson.databind.JsonNode
import dk.fust.docgen.TestHelper
import dk.fust.docgen.model.Documentation
import spock.lang.Specification

class DocumentationServiceSpec extends Specification {

    def "sunshine"() {
        given:
        DocumentationService documentationService = new DocumentationService()
        File testFile = TestHelper.getTestFile('documentation-test.yaml')

        when:
        Documentation documentation = documentationService.loadDocumentation(testFile)

        then:
        noExceptionThrown()
        documentation
    }

    def "read json file"() {
        given:
        DocumentationService documentationService = new DocumentationService()
        File testFile = TestHelper.getTestFile('documentation-test.json')

        when:
        Documentation documentation = documentationService.loadDocumentation(testFile)

        then:
        noExceptionThrown()
        documentation.documentationTitle == 'The documentation title in a JSON file'
    }

}
