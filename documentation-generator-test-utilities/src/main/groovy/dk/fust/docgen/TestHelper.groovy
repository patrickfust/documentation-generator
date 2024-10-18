package dk.fust.docgen

import dk.fust.docgen.model.Documentation
import dk.fust.docgen.service.DocumentationService
import dk.fust.docgen.util.Assert

class TestHelper {

    static File getTestFile(String filename) {
        URL url = TestHelper.classLoader.getResource(filename)
        Assert.isNotNull(url, "url may not be null")
        return new File(url.toURI())
    }

    static Documentation loadTestDocumentation(String filename) {
        DocumentationService documentationService = new DocumentationService()
        return documentationService.loadDocumentation(getTestFile(filename))
    }

}
