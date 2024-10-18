# Documentation Generator

Generates documentation based upon a yaml-file.
Describe how your database looks, in a single file (or files) and then generate the corresponding documentation.

This document covers:
- [Usage in Gradle](#usage-in-gradle)
- [Documentation types](#documentation-types)
  - [Types supported](#types-supported)
- [Demos](#demos)

---

## Usage in Gradle

### Dependency

#### gradle.properties
Add the version of documentation-generator you want to use in `gradle.properties`.
```groovy
documentationGeneratorVersion = 0.0.3
```

#### build.gradle
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

### Gradle task

To generate documentation, run:
```shell
gradle generateDocumentation
```


---

## Documentation types

The documentation generator support these documentation types, but you're free to create your own. 
Just implement `dk.fust.docgen.Generator` and `dk.fust.docgen.GeneratorConfiguration`.

Add a dependency in your buildscript with the corresponding artifact id.

### Documentation types supported

| Artifact id                       | Description                                     |
|-----------------------------------|-------------------------------------------------|
| documentation-generator-erdiagram | Generates entity-relation-diagram               |
| documentation-generator-sqlscript | Generates SQL-files with creation of the tables |


---

## Demos

In the [demos](demos) folder you can see examples on how to use the documentation generator.
