# Documentation Generator SQL Scripts

This generator can generate sql files containing create tables, indexes and views.

## Configuration

To use the sql generator, you must configure it with `dk.fust.docgen.sqlscript.SqlScriptConfiguration`

| Setting           | 	Type             | Description                                                         | 	Default |
|-------------------|-------------------|---------------------------------------------------------------------|----------|
| documentationFile | File              | Location of the documentation file                                  |          |
| destination       | Destination       | Where to send the generated documentation                           |          | 


## Example in a gradle.build
```groovy
new SqlScriptConfiguration(
        documentationFile: new File(projectDir, 'documentation.yaml'),
        destination: new FileDestination(
                directory: new File('scripts'),
                createParentDirectories: true
        )
)
```

## Demo

Check the demo in [demo-sqlscript](../demos/demo-sqlscript)
