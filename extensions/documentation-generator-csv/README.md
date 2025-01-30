# CSV

If you add this module, you can generate documentation as CSV-files

## CSVTableFormatter

Class name: `dk.fust.docgen.csv.format.table.CSVTableFormatter` 

Formats tables as CSV files.

| Setting         | Type               | Description                             | Default   |
|-----------------|--------------------|-----------------------------------------|-----------|
| delimiter       | CSVDelimiter       | Which delimiter to use between fields   | SEMICOLON |  
| recordSeparator | CSVRecordSeparator | Which string to use as record separator | NEWLINE   | 

### CSVDelimiter

Which delimiter to use between fields.

| Value     | Description |
|-----------|-------------|
| SEMICOLON | `;`         |
| COMMA     | `,`         |
| TAB       | `\t`        |
| SPACE     | ` `         |
| PIPE      | `\|`        |

### CSVRecordSeparator

Which string to use as record separator.

| Value                   | Description |
|-------------------------|-------------|
| NEWLINE                 | `\n`        |
| CARRIAGE_RETURN_NEWLINE | `\r\n`      |
| CARRIAGE_RETURN         | `\r`        |
