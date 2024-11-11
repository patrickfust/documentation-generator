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

## Model usage

This generator uses these fields in [Documentation](../../documentation-generator-api/src/main/java/dk/fust/docgen/model/Documentation.java)

```
Documentation
├── databaseName
├── generation
│   ├── generateIdDataType
│   ├── generateId
│   ├── addCreatedAt
│   ├── columnNameCreatedAt
│   ├── addUpdatedAt
│   ├── columnNameUpdatedAt
│   └── triggerForUpdates
└── tables
    ├── generation (same sub fields as above)
    ├── name
    ├── tags
    └── fields
        ├── name
        ├── comment
        ├── transformation
        ├── source
        └── dataType
```

---

## Examples

### gradle.build

```groovy
import dk.fust.docgen.datalineage.DataLineageConfiguration
import dk.fust.docgen.destination.MarkdownDestination
import dk.fust.docgen.format.table.MarkdownTableFormatter

new DataLineageConfiguration(
        key: 'my-data-lineage',
        documentationFile: new File(projectDir, 'documentation.yml'),
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

### generator-configuration.yml

```yaml
- className: dk.fust.docgen.datalineage.DataLineageConfiguration
  documentationFile: documentation.yml
  key: my-data-lineage
  tableFormatter:
    className: dk.fust.docgen.format.table.MarkdownTableFormatter
  destination:
    className: dk.fust.docgen.destination.MarkdownDestination
    file: README.md
```

---

## Demo

Check the demo in [demo-data-lineage](../../demos/demo-data-lineage)
