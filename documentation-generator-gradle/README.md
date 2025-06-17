# Gradle plugin

## Dependency

### gradle.properties
Add the version of documentation-generator you want to use in `gradle.properties`.
```groovy
documentationGeneratorVersion = 1.8.0
```

### build.gradle
Now you can add a dependency to the gradle-plugin.
```groovy
buildscript {
    dependencies {
        classpath("dk.fust:documentation-generator-gradle:${documentationGeneratorVersion}")
    }
}
```
You can instead of the variable `${documentationGeneratorVersion}` simply write the version here.

For every kind of documentation you want generated, you'll need to add the corresponding dependency in the buildscript-section.

For example:
```
classpath("dk.fust:documentation-generator-erdiagram:${documentationGeneratorVersion}")
```

## Gradle task

### Configuration in build.gradle

```groovy
documentationGenerator {
  generatorConfigurations = [
        ... list of configurations ...
  ]
}
```

### Configuration in separate file

Another option is to have your configuration in a separate file.
[See here the description of the configuration file](../README.md#generator-configuration-file)

The list of configurations consists of the desired configurations.

```groovy
documentationGenerator {
    documentationConfigurationFile = new File('generator-configuration.yml')
}
```

## Generate documentation 

To generate documentation, run:
```shell
gradle generateDocumentation
```
