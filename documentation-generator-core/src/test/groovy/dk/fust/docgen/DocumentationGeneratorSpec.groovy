package dk.fust.docgen

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
        }
        DocumentationGenerator documentationGenerator = new DocumentationGenerator()

        when:
        documentationGenerator.generate(documentationConfiguration)

        then:
        noExceptionThrown()
    }

}
