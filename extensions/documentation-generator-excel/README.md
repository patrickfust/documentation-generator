# Excel workbook

This module can create Excel workbooks.

## ExcelBase64TableFormatter

Class name: `dk.fust.docgen.excel.format.table.ExcelBase64TableFormatter`

Converts a FormatTable into an Excel workbook.

Because the generator will encode the binary bytes into base64 encoded String, you can use
[Base64FileDestination](../../README.md#base64filedestination) to save the file as a regular Excel workbook.

| Setting                   | Type                      | Description                                                 | Default                                                                                                                                                                                      |
|---------------------------|---------------------------|-------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| headerExcelStyle          | [ExcelStyle](#excelstyle) | Style of the top header                                     | fontName: `Verdana` <br/>fontHeightInPoints `11`<br/>fontColor: `IndexedColors.WHITE`<br/>backgroundColor: `IndexedColors.DARK_BLUE`<br/>bold: `true`<br/>borderColor: `IndexedColors.WHITE` |
| secondaryHeaderExcelStyle | [ExcelStyle](#excelstyle) | Style of the following headers                              | fontName: `Verdana` <br/>fontHeightInPoints `9`<br/>fontColor: `IndexedColors.WHITE`<br/>backgroundColor: `IndexedColors.LIGHT_BLUE`<br/>bold: `true`<br/>borderColor: `IndexedColors.WHITE` |
| evenRowExcelStyle         | [ExcelStyle](#excelstyle) | Style of even rows                                          | fontName: `Verdana` <br/>fontHeightInPoints `9`<br/>fontColor: `IndexedColors.BLACK`<br/>backgroundColor: `220, 220, 220`<br/>bold: `false`<br/>borderColor: `IndexedColors.WHITE`           |
| oddRowExcelStyle          | [ExcelStyle](#excelstyle) | Style of even rows                                          | fontName: `Verdana` <br/>fontHeightInPoints `9`<br/>fontColor: `IndexedColors.BLACK`<br/>backgroundColor: `240, 240, 240`<br/>bold: `false`<br/>borderColor: `IndexedColors.WHITE`           |
| defaultColumnWidth        | int                       | Sets the default column width                               | 50                                                                                                                                                                                           |
| autoResizeColumns         | List<Integer>             | After setting all cells, these columns will call autoResize | empty                                                                                                                                                                                        |
| autofilter                | boolean                   | Sets auto filter                                            | true                                                                                                                                                                                         |

### ExcelStyle

An `ExcelStyle` contains how a cell may be formatted in Excel.

The defaults for the different cells, can be found [just above](#excelbase64tableformatter).

| Setting            | Type                      | Description      | 
|--------------------|---------------------------|------------------|
| fontName           | String                    | Font name        |
| fontHeightInPoints | short                     | Height in point  |
| fontColor          | [ExcelColor](#excelcolor) | Font color       |
| backgroundColor    | [ExcelColor](#excelcolor) | Background color |
| bold               | boolean                   | bold or not      |
| borderColor        | [ExcelColor](#excelcolor) | Border color     |

### ExcelColor

The ExcelColor can either contain a name of a color in `IndexedColors` or a RGB set.

#### From IndexedColors
```yaml
fontColor: WHITE
```

#### From RGB
```yaml
fontColor: 123, 22, 0
```

## Configuration

An example on how to use the configure `ExcelBase64TableFormatter`

```yaml
  tableFormatter:
    className: dk.fust.docgen.excel.format.table.ExcelBase64TableFormatter
    headerExcelStyle:
      fontName: Verdana
      fontHeightInPoints: 11
      fontColor: WHITE
      bold: true
      backgroundColor: GREY_25_PERCENT
    secondaryHeaderExcelStyle:
      fontName: Verdana
      fontHeightInPoints: 9
      fontColor: WHITE
      bold: true
      backgroundColor: GREY_50_PERCENT
    evenRowExcelStyle:
      fontName: Verdana
      fontHeightInPoints: 9
      fontColor: WHITE
      bold: false
      backgroundColor: 1, 2, 3
    oddRowExcelStyle:
      fontName: Times New Roman
      fontHeightInPoints: 10
      fontColor: 34, 77, 55
      bold: false
      backgroundColor: 1, 2, 3
    defaultColumnWidth: 50
    autoResizeColumns:
      - 0
      - 1
      - 2
      - 3
```

## Demo

See [demo-data-dictionary](../../demos/demo-data-dictionary) for demo.
