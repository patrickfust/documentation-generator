# Documentation Generator Data Dictionary

This generator can generate [data dictionaries](https://atlan.com/what-is-a-data-dictionary/).

It consists of two parts: [Generate Data Dictionary](#generate-data-dictionary) 
and [Generate Data Dictionary Meta Infomation](#generate-data-dictionary-meta-information).

# Generate Data Dictionary

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
| exportFilename        | boolean        | Should the filename be in the export?                        | true                     |
| exportColumn          | boolean        | Should the column be in the export?                          | true                     |
| exportPosition        | boolean        | Should the position be in the export?                        | true                     |
| exportDataType        | boolean        | Should the data type be in the export?                       | true                     |
| exportMandatory       | boolean        | Should the mandatory field be in the export?                 | true                     |
| exportKeys            | boolean        | Should the keys be in the export?                            | true                     |
| exportDescription     | boolean        | Should the description be in the export?                     | true                     |

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

# Generate Data Dictionary Meta Information

In order to get meta information about the data dictionary files, you may use `DataDictionaryMetaInfoConfiguration`. 

## Configuration

To use the data dictionary meta info generator, you must configure it with `dk.fust.docgen.datadict.DataDictionaryMetaInfoConfiguration`

| Setting               | 	Type          | Description                                                  | Default                  |
|-----------------------|----------------|--------------------------------------------------------------|--------------------------|
| documentationFile     | File           | Location of the documentation file                           |                          |
| destination           | Destination    | Where to send the generated documentation                    |                          | 
| key                   | String         | Key to identify it on the destination                        |                          |
| tableFormatter        | TableFormatter | Formatter that can create the table                          | `MarkdownTableFormatter` |
| filter                | String         | If specified, only tables with this filter is used           |                          | 

## Model usage

This generator uses these fields in [Documentation](../../documentation-generator-api/src/main/java/dk/fust/docgen/model/Documentation.java)

```
Documentation
└── dataDictionary
    └── dataDictionaryFiles
        ├── fileName
        ├── fileDescription
        └── version
```

---

## Examples

### gradle.build

```groovy
import dk.fust.docgen.datadict.DataDictionaryMetaInfoConfiguration
import dk.fust.docgen.destination.FileDestination
import dk.fust.docgen.format.table.JsonTableFormatter

new DataDictionaryMetaInfoConfiguration(
        documentationFile: new File(projectDir, 'data-dictionary.yml'),
        destination: new FileDestination(
                file: new File('data-dictionary-meta-info.json'),
        ),
        tableFormatter: new JsonTableFormatter(),
)
```

### generator-configuration.yml

```yaml
- className: dk.fust.docgen.datadict.DataDictionaryMetaInfoConfiguration
  documentationFile: data-dictionary.yml
  tableFormatter:
    className: dk.fust.docgen.format.table.JsonTableFormatter
  destination:
    className: dk.fust.docgen.destination.FileDestination
    file: data-dictionary-meta-info.json
```

---

## Demo

Check the demo in [demo-data-dictionary](../../demos/demo-data-dictionary)
