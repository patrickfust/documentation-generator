# Confluence

If you add this module, you can send documentation to Confluence.

## ConfluenceDestination

Class name: `dk.fust.docgen.confluence.destination.ConfluenceDestination`

### Setup
In order to use Confluence as destination, you'll need to add this module in your build.gradle:
```groovy
buildscript {
    dependencies {
        classpath("dk.fust.docgen:documentation-generator-gradle:${documentationGeneratorVersion}")
        classpath("dk.fust.docgen:documentation-generator-confluence:${documentationGeneratorVersion}")
    }
}
```

### Configuration of destination

| Setting             | Type   | Description             | Exanple                                 |
|---------------------|--------|-------------------------|-----------------------------------------|
| baseUrl             | String | Base URL to Confluence. | https://xxx.atlassian.net/wiki/rest/api |
| documentationPageId | String | Id of the parent page.  | 123456                                  |
| spaceKey            | String | Confluence space key    | ABC                                     |
| parentPageTitle     | String |                         | My Parent Page                          |
| pageTitle           | String |                         | My Page                                 | 

Username and personal access token for Confluence will be promptet.
