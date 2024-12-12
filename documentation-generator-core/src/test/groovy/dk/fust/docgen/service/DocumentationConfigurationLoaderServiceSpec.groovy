package dk.fust.docgen.service

import dk.fust.docgen.GeneratorConfiguration
import dk.fust.docgen.MockEnum
import dk.fust.docgen.MockGenerator
import dk.fust.docgen.MockGeneratorConfiguration
import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MarkdownDestination
import spock.lang.Specification

class DocumentationConfigurationLoaderServiceSpec extends Specification {

    def "read documentation configuration"() {
        given:
        DocumentationConfigurationLoaderService service = new DocumentationConfigurationLoaderService()
        File file = TestHelper.getTestFile('generator-configuration.yml')

        when:
        List<GeneratorConfiguration> conf = service.readConfigurations(file)

        then:
        conf.size() == 2
        conf.first() instanceof MockGeneratorConfiguration
        MockGeneratorConfiguration mockGeneratorConfiguration = conf.first() as MockGeneratorConfiguration
        mockGeneratorConfiguration.getDocumentationFile().name == 'documentation.yaml'
        mockGeneratorConfiguration.getDestination() instanceof MarkdownDestination
        MarkdownDestination markdownDestination = mockGeneratorConfiguration.getDestination() as MarkdownDestination
        markdownDestination.file.name == 'README.md'
        mockGeneratorConfiguration.anInt == 1
        mockGeneratorConfiguration.aBigInteger == 2
        mockGeneratorConfiguration.aLong == 3
        mockGeneratorConfiguration.aBigLong == 4
        mockGeneratorConfiguration.aString == "some string"
        mockGeneratorConfiguration.aBoolean
        mockGeneratorConfiguration.aBigBoolean
        mockGeneratorConfiguration.mockEnum == MockEnum.B
    }

    def "read bad documentation configuration where field don't exist"() {
        given:
        DocumentationConfigurationLoaderService service = new DocumentationConfigurationLoaderService()
        File file = TestHelper.getTestFile('generator-configuration-field-not-exists.yml')

        when:
        service.readConfigurations(file)

        then:
        thrown(IllegalArgumentException)
    }

}
