package dk.fust.docgen

import dk.fust.docgen.destination.MockDestination
import spock.lang.Specification

class DocumentationGeneratorSpec extends Specification {

    def "generate documentation"() {
        given:
        Generator generator = Mock(Generator) {
            1 * generate(_, _)
        }
        GeneratorConfiguration documentationConfiguration = Mock(GeneratorConfiguration) {
            1 * getDocumentationFile() >> {
                return TestHelper.getTestFile('documentation-test.yaml')
            }
            1 * getGenerator() >> {
                return generator
            }
            _ * getDestination() >> {
                return new MockDestination()
            }
        }
        DocumentationGenerator documentationGenerator = new DocumentationGenerator()

        when:
        documentationGenerator.generate(documentationConfiguration)

        then:
        noExceptionThrown()
    }

}
