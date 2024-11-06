# Documentation Generator ER-diagram

This generator can generate entity-relation diagrams in the format of [PlantUML](https://plantuml.com/) and [Mermaid](https://mermaid.js.org/)

## Configuration

To use the entity-relation diagram generator, you must configure it with `dk.fust.docgen.erdiagram.ERDiagramConfiguration`

| Setting           | 	Type             | Description                                                        | 	Default |
|-------------------|-------------------|--------------------------------------------------------------------|----------|
| documentationFile | File              | Location of the documentation file                                 |          |
| destination       | Destination       | Where to send the generated documentation                          |          | 
| umlGenerator      | UMLGenerator      | Which type of ER-diagram to generate. Can be MERMAID and PLANTUML  | MERMAID  |
| generateKeys      | List<GenerateKey> | List of model-tags and the placeholder to replace in documentation |          | 

### GenerateKey

`GenerateKey` identifies a sub-set of the tables to generate an ER-diagram for.
You can group them for instance into domain-model and external-reference and so forth, by adding a filter.
Only tables with a tag matching the filter will be used.

If the `filter` is `null`, then all tables are selected.

The `destinationKey` tells which part of the destination that needs replacing.

For instance if you're using `MarkdownDestination`, you can point to the same markdown-file but with different `destinationKey`, 
so you can have several ER-diagram with a sub-set of the tables.

Read more about `MarkdownDestination` [here](../../README.md#dkfustdocgendestinationmarkdowndestination-)

## Examples

### gradle.build

```groovy
import dk.fust.docgen.erdiagram.GenerateKey
import dk.fust.docgen.erdiagram.generators.UMLGenerator
import dk.fust.docgen.destination.MarkdownDestination
import dk.fust.docgen.erdiagram.ERDiagramConfiguration

new ERDiagramConfiguration(
    documentationFile: new File(projectDir, 'documentation.yml'),
    umlGenerator : UMLGenerator.MERMAID, // Can be omitted because it's default
    generateKeys: [
            // Empty filter means all groups
            new GenerateKey(destinationKey: 'MODEL_MERMAID_PLACEHOLDER'),
            new GenerateKey(destinationKey: 'MODEL_MERMAID_GROUP_PLACEHOLDER', filter: 'my_group')
    ],
    destination: new MarkdownDestination(
            file: new File('README.md'),
    )
)
```

### generator-configuration.yml

```yaml
- className: dk.fust.docgen.erdiagram.ERDiagramConfiguration
  documentationFile: documentation.yml
  umlGenerator: MERMAID
  generateKeys:
    - className: dk.fust.docgen.erdiagram.GenerateKey
      destinationKey: MODEL_MERMAID_PLACEHOLDER
    - className: dk.fust.docgen.erdiagram.GenerateKey
      destinationKey: MODEL_MERMAID_GROUP_PLACEHOLDER
      filter: my_group
  destination:
    className: dk.fust.docgen.destination.MarkdownDestination
    file: README.md
```

## Demo

Check the demo in [demo-erdiagram](../../demos/demo-erdiagram)
