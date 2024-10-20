# Documentation Generator ER-diagram

This generator can generate entity-relation diagrams in the format of [PlantUML](https://plantuml.com/) and [Mermaid](https://mermaid.js.org/)

## Configuration

To use the entity-relation diagram generator, you must configure it with `dk.fust.docgen.erdiagram.ERDiagramConfiguration`

| Setting           | 	Type             | Description                                                         | 	Default |
|-------------------|-------------------|---------------------------------------------------------------------|----------|
| documentationFile | File              | Location of the documentation file                                  |          |
| destination       | Destination       | Where to send the generated documentation                           |          | 
| umlGenerator      | UMLGenerator      | Which type of ER-diagram to generate. Can be MERMAID and PLANTUML   | MERMAID  |
| generateKeys      | List<GenerateKey> | List of model-group and the placeholder to replace in documentation |          | 

### GenerateKey

`GenerateKey` identifies a sub-set of the tables to generate an ER-diagram for.
You can group them for instance into domain-model and external-reference and so forth.

If the `group` is `null`, then all tables are selected.

The `destinationKey` tells which part of the destination that needs replacing.

For instance if you're using `MarkdownDestination`, you can point to the same markdown-file but with different `destinationKey`, 
so you can have several ER-diagram with a sub-set of the tables.

Read more about `MarkdownDestination` [here](../README.md#dkfustdocgendestinationmarkdowndestination-)

## Example in a gradle.build
```groovy
new ERDiagramConfiguration(
    documentationFile: new File(projectDir, 'documentation.yaml'),
    umlGenerator : UMLGenerator.MERMAID, // Can be omitted because it's default
    generateKeys: [
            // Empty group means all groups
            new GenerateKey(destinationKey: 'MODEL_MERMAID_PLACEHOLDER'),
            new GenerateKey(destinationKey: 'MODEL_MERMAID_GROUP_PLACEHOLDER', group: 'my_group')
    ],
    destination: new MarkdownDestination(
            file: new File('README.md'),
    )
)
```

## Demo

Check the demo in [demo-erdiagram](../demos/demo-erdiagram)
