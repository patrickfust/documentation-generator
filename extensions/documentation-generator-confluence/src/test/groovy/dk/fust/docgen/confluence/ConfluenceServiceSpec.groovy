package dk.fust.docgen.confluence

import dk.fust.docgen.confluence.model.Metadata
import dk.fust.docgen.confluence.model.Page
import dk.fust.docgen.confluence.model.Properties
import dk.fust.docgen.confluence.model.PropertyValue
import dk.fust.docgen.confluence.model.Space
import spock.lang.Specification

class ConfluenceServiceSpec extends Specification {

    def "serialize page"() {
        given:
        Page page = new Page(
                space: new Space('confluence_space_key'),
                ancestors: [new Page(id: 'confluence_ancestor_page_id')],
                metadata: Metadata.fullPageWidthMetadata()
        )
        ConfluenceService confluenceService = new ConfluenceService(null, null, null)

        when:
        String json = confluenceService.pageToString(page)

        then:
        json
        json.contains 'content-appearance-draft'
    }
}
