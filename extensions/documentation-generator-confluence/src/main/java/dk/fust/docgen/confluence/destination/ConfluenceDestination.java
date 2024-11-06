package dk.fust.docgen.confluence.destination;

import dk.fust.docgen.UserInput;
import dk.fust.docgen.confluence.ConfluenceService;
import dk.fust.docgen.confluence.model.Page;
import dk.fust.docgen.confluence.model.Storage;
import dk.fust.docgen.destination.Destination;
import dk.fust.docgen.util.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Destination when sending to Confluence
 */
@Slf4j
@Data
public class ConfluenceDestination implements Destination {

    private static final String EMPTY_PARAGRAPH = "<p></p>";

    private String baseUrl;
    private String documentationPageId;
    private String spaceKey;

    private String parentPageTitle;
    private String pageTitle;

    private UserInput username = new UserInput("Enter your Confluence username");
    private UserInput accessToken = new UserInput("Enter your Confluence personal access token");

    @Override
    public void validate() {
        Assert.isNotNull(baseUrl, "baseUrl must not be null");
        Assert.isNotNull(spaceKey, "spaceKey must not be null");
        Assert.isNotNull(documentationPageId, "documentationPageId must not be null");
        Assert.isNotNull(pageTitle, "pageTitle must not be null");
        Assert.isNotNull(username, "username must not be null");
        Assert.isNotNull(accessToken, "accessToken must not be null");
    }

    @Override
    public void sendDocumentToDestination(String document, String destination) throws IOException {
        if (username.getAnswer() != null && !username.getAnswer().isEmpty()
                && accessToken.getAnswer() != null && !accessToken.getAnswer().isEmpty()) {
            ConfluenceService confluenceService = new ConfluenceService(baseUrl, username.getAnswer(), accessToken.getAnswer());

            String id = documentationPageId;
            if (parentPageTitle != null) {
                Page parentPage = getOrCreateChildWithTitle(documentationPageId, parentPageTitle, confluenceService);
                id = parentPage.getId();
            }
            Page page = getOrCreateChildWithTitle(id, pageTitle, confluenceService);

            log.debug("Got page: {}", page);

            String originalHtml = page.getBody().getStorage().getValue();
            replaceTable(page, document, destination);
            if (!originalHtml.equals(page.getBody().getStorage().getValue())) {
                log.debug("Table has changed -> updating Confluence");
                page.getVersion().setNumber(page.getVersion().getNumber() + 1);
                confluenceService.updatePage(page);
                log.info("Table is updated");
            } else {
                log.info("Table has not changed -> Won't update Confluence");
            }
        }
    }

    private void replaceTable(Page page, String confluenceTable, String destination) {
        Storage storage = page.getBody().getStorage();
        String html = storage.getValue();
        if (html.contains("<table class=\"%s".formatted(destination))) {
            String tableReplacedHtml = html.replaceAll("(.*)<table class=\"datalineage.*</table>(.*)", "$1%s$2".formatted(confluenceTable));
            storage.setValue(tableReplacedHtml);
        } else {
            // No replacement - None existing table
            storage.setValue(confluenceTable);
        }
    }

    private Page getOrCreateChildWithTitle(String pageId, String childTitle, ConfluenceService confluenceService) throws IOException {
        Page page = confluenceService.getChildWithTitle(pageId, childTitle);
        if (page == null) {
            page = confluenceService.createChildPage(pageId, childTitle, spaceKey, EMPTY_PARAGRAPH);
        }
        Assert.isNotNull(page, "Could not find or create page with title " + childTitle);
        return page;
    }

}
