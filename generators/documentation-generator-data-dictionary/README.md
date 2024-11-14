# Documentation Generator Data Dictionary

This generator can generate [data dictionaries](https://atlan.com/what-is-a-data-dictionary/).

## Configuration

To use the data dictionary generator, you must configure it with `dk.fust.docgen.datadict.DataDictionaryConfiguration`

| Setting               | 	Type          | Description                                                  | Default                  |
|-----------------------|----------------|--------------------------------------------------------------|--------------------------|
| documentationFile     | File           | Location of the documentation file                           |                          |
| destination           | Destination    | Where to send the generated documentation                    |                          | 
| key                   | String         | Key to identify it on the destination                        |                          |
| tableFormatter        | TableFormatter | Formatter that can create the table                          | `MarkdownTableFormatter` |
| addDescriptionForFile | boolean        | Will add a header row containing the description of the file | false                    | 
| filterTags            | String         | If specified, only tables with this filter is used           |                          | 

## Model usage

This generator uses these fields in [Documentation](../../documentation-generator-api/src/main/java/dk/fust/docgen/model/Documentation.java)

```
Documentation
└── dataDictionary
    └── dataDictionaryFiles
        ├── fileName
        ├── fileDescription
        ├── version
        ├── columns
        │   ├── columnName
        │   ├── columnDescription
        │   ├── dataType
        │   ├── regex
        │   ├── example
        │   ├── mandatory
        │   ├── minimumValue
        │   ├── maximumValue
        │   └── keys
        └── tags
```

---

## Examples

### gradle.build

```groovy
import dk.fust.docgen.datadict.DataDictionaryConfiguration
import dk.fust.docgen.destination.MarkdownDestination
import dk.fust.docgen.format.table.MarkdownTableFormatter

new DataDictionaryConfiguration(
        documentationFile: new File(projectDir, 'data-dictionary.yml'),
        destination: new MarkdownDestination(
                file: new File('data-dictionary.md'),
        ),
        tableFormatter: new MarkdownTableFormatter(),
)
```

### generator-configuration.yml

```yaml
- className: dk.fust.docgen.datadict.DataDictionaryConfiguration
  documentationFile: data-dictionary.yml
  tableFormatter:
    className: dk.fust.docgen.format.table.MarkdownTableFormatter
  destination:
    className: dk.fust.docgen.destination.MarkdownDestination
    file: data-dictionary.md
```

---

## Demo

Check the demo in [demo-data-dictionary](../../demos/demo-data-dictionary)
