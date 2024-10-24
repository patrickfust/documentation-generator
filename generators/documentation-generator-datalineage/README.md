# Documentation Generator Data lineage

This generator can generate [data lineage](https://www.ibm.com/topics/data-lineage).

## Configuration

To use the entity-relation diagram generator, you must configure it with `dk.fust.docgen.erdiagram.ERDiagramConfiguration`

| Setting                  | 	Type             | Description                                                                       | Default                  |
|--------------------------|-------------------|-----------------------------------------------------------------------------------|--------------------------|
| documentationFile        | File              | Location of the documentation file                                                |                          |
| destination              | Destination       | Where to send the generated documentation                                         |                          | 
| key                      | String            | Key to identify it on the destination                                             |                          |
| sourceDocumentationFiles | Map<String, File> | External references to other documentationfiles. Is used when specifying `source` |                          |
| tableFormatter           | TableFormatter    | Formatter that can create the table                                               | `MarkdownTableFormatter` |
| filter                   | String            | If specified, only tables with this filter is used                                |                          | 

## Example in a gradle.build

```groovy
new DataLineageConfiguration(
        key: 'my-data-lineage',
        documentationFile: new File(projectDir, 'documentation.yaml'),
        destination: new MarkdownDestination(
                file: new File('README.md'),
        ),
        tableFormatter: new MarkdownTableFormatter(),
        sourceDocumentationFiles: [
                'external' : new File(projectDir, 'external-documentation.yaml')
                'external2' : new File(projectDir, 'external2-documentation.yaml')
        ]
)
```

## Demo

Check the demo in [demo-datalineage](../../demos/demo-datalineage)
