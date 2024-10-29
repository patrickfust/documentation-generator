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
Go to the subfolder `documentation-generator-maven-plugin`

1. Build the Maven plugin: `mvn install`
2. Publish it locally: `mvn -Ppublication`
3. Release it: `jreleaser full-release --git-root-search`
