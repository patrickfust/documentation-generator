package dk.fust.docgen.confluence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.fust.docgen.confluence.model.Body;
import dk.fust.docgen.confluence.model.Metadata;
import dk.fust.docgen.confluence.model.Page;
import dk.fust.docgen.confluence.model.PageSearch;
import dk.fust.docgen.confluence.model.Space;
import dk.fust.docgen.confluence.model.Storage;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.AbstractHttpMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Service to integrate to Confluence
 */
@Slf4j
public class ConfluenceService {

    private final String baseUrl;
    private final String basicAuth;
    private final ObjectMapper objectMapper;

    /**
     * Constructor
     * @param baseUrl URL to Confluence
     * @param username your username
     * @param accessToken personal access token
     */
    public ConfluenceService(String baseUrl, String username, String accessToken) {
        this.baseUrl = baseUrl;
        String basicAuth = "%s:%s".formatted(username, accessToken);
        this.basicAuth = Base64.getMimeEncoder().encodeToString(basicAuth.getBytes(StandardCharsets.UTF_8));
        objectMapper = new ObjectMapper();
    }

    /**
     * Retrieve page
     * @param id page to fetch
     * @return loaded page
     * @throws IOException an error occurred
     */
    public Page getPage(String id) throws IOException {
        String url = "%s/content/%s".formatted(baseUrl, id);
        HttpGet get = new HttpGet("%s?expand=body.storage,version".formatted(url));
        return callAndParse(get, Page.class);
    }

    /**
     * Update a Confluence page
     * @param page update
     * @return the updated page
     * @throws IOException an error occurred
     */
    public Page updatePage(Page page) throws IOException {
        page.setAncestors(null); // Don't want to mess with ancestors
        page.setSpace(null); // Don't want to mess with spaces
        String url = "%s/content/%s".formatted(baseUrl, page.getId());
        HttpPut put = new HttpPut(url);
        String json = pageToString(page);
        log.info("Updating Confluence with: {}", json);
        put.setEntity(new StringEntity(json));
        return callAndParse(put, Page.class);
    }

    /**
     * Get a child page for the id, with the titleForChild
     * @param id parent id
     * @param titleForChild title to search for
     * @return found page or null
     * @throws IOException an error occurred
     */
    public Page getChildWithTitle(String id, String titleForChild) throws IOException {
        String url = "%s/content/%s/child".formatted(baseUrl, id);
        HttpGet get = new HttpGet("%s?expand=page&limit=100".formatted(url));
        PageSearch pageSearch = callAndParse(get, PageSearch.class);
        if (pageSearch.getPage() != null && pageSearch.getPage().getResults() != null && !pageSearch.getPage().getResults().isEmpty()) {
            log.info("has child pages");
            Optional<Page> pages = pageSearch.getPage().getResults().stream().filter(page -> page.getTitle().equals(titleForChild)).findFirst();
            if (pages.isPresent()) {
                Page childPageInfo = pages.get();
                log.info("Found child page with title: {}", childPageInfo.getTitle());
                return getPage(childPageInfo.getId());
            }
        }
        return null;
    }

    /**
     * Create a child page
     * @param id parent id
     * @param childTitle page's title
     * @param spaceKey space
     * @param html content of the page
     * @return the created page
     * @throws IOException an error occurred
     */
    public Page createChildPage(String id, String childTitle, String spaceKey, String html) throws IOException {
        String url = "%s/content/".formatted(baseUrl);
        HttpPost post = new HttpPost(url);
        Page createPage = new Page();
        createPage.setType("page");
        createPage.setTitle(childTitle);
        createPage.setMetadata(Metadata.fullPageWidthMetadata());
        Page ancestor = new Page();
        ancestor.setId(id);
        createPage.setAncestors(List.of(ancestor));
        createPage.setSpace(new Space(spaceKey));
        createPage.setBody(new Body(new Storage(html, "storage")));
        String json = pageToString(createPage);
        post.setEntity(new StringEntity(json));
        return callAndParse(post, Page.class);
    }

    private <T> T callAndParse(HttpRequestBase request, Class<T> clazz) throws IOException {
        addHeaders(request, basicAuth);

        CloseableHttpResponse response;
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            response = client.execute(request);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("Result from Confluence: {}", bufferedReader.readLine());
                throw new RuntimeException("Confluence call failed. HttpCode: " + response.getStatusLine().getStatusCode() + ". Uri: " + request.getRequestLine().getUri());
            }
            return objectMapper.readValue(bufferedReader, clazz);
        }
    }

    private static void addHeaders(AbstractHttpMessage message, String basicAuth) {
        message.addHeader("content-type", "application/json");
        message.addHeader("Authorization", "Basic " + basicAuth);
    }

    /**
     * Convert page to json
     * @param page page to convert
     * @return json representation of the page
     * @throws JsonProcessingException an error occurred
     */
    protected String pageToString(Page page) throws JsonProcessingException {
        return objectMapper.writeValueAsString(page);
    }
}
