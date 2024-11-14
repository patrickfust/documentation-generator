package dk.fust.docgen.erdiagram

import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MarkdownDestination
import spock.lang.Specification

class ERDiagramGeneratorSpec extends Specification {

    void "generate ER-diagram"() {
        given:
        File markdownFile = File.createTempFile('README', '.md')
        markdownFile << """
Leading stuff

[//]: #SOME_PLACEHOLDER_START ()
SOMETHING TO BE REPLACED
[//]: #SOME_PLACEHOLDER_END ()

Trailing stuff
"""
        ERDiagramConfiguration erDiagramConfiguration = new ERDiagramConfiguration(
                destination: new MarkdownDestination(file: markdownFile),
                generateKeys: [
                        new GenerateKey(filterTags: 'domain-model', destinationKey: 'SOME_PLACEHOLDER')
                ]
        )

        when:
        new ERDiagramGenerator().generate(TestHelper.loadTestDocumentation('documentation-erdiagram.yaml'), erDiagramConfiguration)

        then:
        noExceptionThrown()

        and:
        String markdownText = markdownFile.text
        markdownText.contains 'Leading stuff'
        markdownText.contains 'Trailing stuff'
        !markdownText.contains('SOMETHING TO BE REPLACED')
    }

}
