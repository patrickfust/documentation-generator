package dk.fust.docgen.excel

import dk.fust.docgen.DocumentationGenerator
import dk.fust.docgen.GeneratorConfiguration
import dk.fust.docgen.TestData
import dk.fust.docgen.TestHelper
import dk.fust.docgen.datadict.DataDictionaryConfiguration
import dk.fust.docgen.destination.Base64FileDestination
import dk.fust.docgen.excel.format.table.ExcelBase64TableFormatter
import dk.fust.docgen.format.table.FormatTable
import dk.fust.docgen.service.DocumentationConfigurationLoaderService
import org.apache.poi.ss.usermodel.IndexedColors
import spock.lang.Specification

class ExcelBase64TableFormatterSpec extends Specification {

    def "generate Excel"() {
        given:
        ExcelBase64TableFormatter excelTableFormatter = new ExcelBase64TableFormatter()
        FormatTable table = TestData.generateTable()
        Base64FileDestination base64FileDestination = new Base64FileDestination(file: new File('build/excel.xlsx'))

        when:
        String excelBase64Encoded = excelTableFormatter.formatTable(table)
        base64FileDestination.sendDocumentToDestination(excelBase64Encoded, null);

        then:
        noExceptionThrown()
        excelBase64Encoded
    }

    def "read using generator configuration"() {
        given:
        DocumentationConfigurationLoaderService service = new DocumentationConfigurationLoaderService()

        when:
        List<GeneratorConfiguration> configurations = service.readConfigurations(TestHelper.getTestFile('generator-configuration.yml'))

        then:
        configurations.size() == 1
        configurations[0] instanceof DataDictionaryConfiguration
        DataDictionaryConfiguration ddc = configurations[0] as DataDictionaryConfiguration
        ddc.tableFormatter instanceof ExcelBase64TableFormatter
        ExcelBase64TableFormatter excelBase64TableFormatter = ddc.tableFormatter as ExcelBase64TableFormatter
        excelBase64TableFormatter.headerExcelStyle.fontName == "Verdana"
        excelBase64TableFormatter.headerExcelStyle.fontColor.getIndexedColor() == IndexedColors.WHITE
        excelBase64TableFormatter.headerExcelStyle.backgroundColor.getIndexedColor()
        excelBase64TableFormatter.columnCustomizations.size() == 0

        when: 'generating excel'
        DocumentationGenerator documentationGenerator = new DocumentationGenerator();
        documentationGenerator.generate(configurations)

        then:
        noExceptionThrown()
    }

    def "read using generator configuration with column widths"() {
        given:
        DocumentationConfigurationLoaderService service = new DocumentationConfigurationLoaderService()

        when:
        List<GeneratorConfiguration> configurations = service.readConfigurations(TestHelper.getTestFile('generator-configuration-column-and-row.yml'))

        then:
        configurations.size() == 1
        DataDictionaryConfiguration ddc = configurations[0] as DataDictionaryConfiguration
        ExcelBase64TableFormatter excelBase64TableFormatter = ddc.tableFormatter as ExcelBase64TableFormatter


        excelBase64TableFormatter.columnCustomizations.size() == 3

        excelBase64TableFormatter.columnCustomizations[0].columnNumber == 0
        excelBase64TableFormatter.columnCustomizations[0].columnDescription == null
        excelBase64TableFormatter.columnCustomizations[0].columnWidth == 20
        excelBase64TableFormatter.columnCustomizations[0].autoResize == null

        excelBase64TableFormatter.columnCustomizations[1].columnNumber == null
        excelBase64TableFormatter.columnCustomizations[1].columnDescription == 'Filename'
        excelBase64TableFormatter.columnCustomizations[1].columnWidth == 30
        excelBase64TableFormatter.columnCustomizations[1].autoResize == null

        excelBase64TableFormatter.columnCustomizations[2].columnNumber == null
        excelBase64TableFormatter.columnCustomizations[2].columnDescription == 'Type'
        excelBase64TableFormatter.columnCustomizations[2].columnWidth == null
        excelBase64TableFormatter.columnCustomizations[2].autoResize == true

        when: 'generating excel'
        DocumentationGenerator documentationGenerator = new DocumentationGenerator()
        documentationGenerator.generate(configurations)

        then:
        noExceptionThrown()
    }

}
