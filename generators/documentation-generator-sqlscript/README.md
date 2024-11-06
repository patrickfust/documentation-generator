# Documentation Generator SQL Scripts

This generator can generate sql files containing create tables, indexes and views.

## Configuration

To use the sql generator, you must configure it with `dk.fust.docgen.sqlscript.SqlScriptConfiguration`

| Setting           | 	Type      | Description                                        | Default   |
|-------------------|-------------|----------------------------------------------------|-----------|
| documentationFile | File        | Location of the documentation file                 |           |
| destination       | Destination | Where to send the generated documentation          |           | 
| filter            | String      | If specified, only tables with this filter is used |           | 

## Examples

### gradle.build

```groovy
import dk.fust.docgen.sqlscript.SqlScriptConfiguration
import dk.fust.docgen.destination.DirectoryDestination

new SqlScriptConfiguration(
        documentationFile: new File(projectDir, 'documentation.yml'),
        destination: new DirectoryDestination(
                directory: new File('scripts'),
                createParentDirectories: true
        )
)
```

### generator-configuration.yml

```yaml
- className: dk.fust.docgen.sqlscript.SqlScriptConfiguration
  documentationFile: documentation.yml
  destination:
    className: dk.fust.docgen.destination.DirectoryDestination
    directory: scripts
    createParentDirectories: true
```

## Demo

Check the demo in [demo-sqlscript](../../demos/demo-sqlscript)
