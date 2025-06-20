# Releasing

This documents describes how to release Documentation Generator.

It uses [JReleaser](https://jreleaser.org/) to help package, sign and upload releases to [GitHub](https://github.com/patrickfust/documentation-generator) and [Maven Central](https://central.sonatype.com/search?q=dk.fust).

It consists of these parts:
1. Core functionality
2. Different generator plugins
3. Different destination plugins
4. Gradle plugin
5. Maven plugin

Every part (except the Maven plugin) can be released in the root folder.

## Release all except Maven plugin
1. Build the packages with: `./gradlew publish`
2. Release to GitHub and Maven Central with: `./gradlew jreleaserFullRelease`

## Release Maven plugin

1. Go to the subfolder `documentation-generator-maven-plugin`
2. Build the Maven plugin: `mvn clean install -U` (-U is force update from Maven central)
3. Publish it locally: `mvn -Ppublication`
4. Release it: `jreleaser full-release --git-root-search`

# JRelease schema

In order to update the JRelease schema, you can run the following command:

```bash
brew upgrade jreleaser
jreleaser json-schema
```
