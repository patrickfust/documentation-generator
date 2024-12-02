package dk.fust.docgen.excel.format.table.model;

import dk.fust.docgen.util.Assert;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.Color;

/**
 * A color in Excel
 */
@Data
public class ExcelColor {

    private IndexedColors indexedColor;

    private XSSFColor xssfColor;

    private String value;

    /**
     * Default empty constructor
     */
    public ExcelColor() {
    }

    /**
     * Initializing with a color.
     * @see #setValue(String) setValue(String) for format
     * @param color color
     */
    public ExcelColor(String color) {
        setValue(color);
    }

    /**
     * Using an indexedColor
     * @param indexedColor Color from IndexedColors
     */
    public ExcelColor(IndexedColors indexedColor) {
        this.indexedColor = indexedColor;
    }

    /**
     * Can handle both IndexedColors as well as RGB.
     * Can either be {@link IndexedColors} or a RGB value on the form 'x, y, z'
     * @see IndexedColors IndexedColors for which colors to choose from
     * @param color color
     */
    public void setValue(String color) {
        Assert.isNotNull(color, "color must not be null");
        this.value = value;
        try {
            indexedColor = IndexedColors.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException iae) {
            // Parse the RGB values
            String[] rgbString = color.trim().split(",");
            Assert.isTrue(rgbString.length == 3, "Invalid color format. Must be on form: '1,2,3'");
            xssfColor = new XSSFColor(makeColor(rgbString), new DefaultIndexedColorMap());
        }
    }

    private Color makeColor(String[] rgbString) {
        return new Color(
                Integer.parseInt(rgbString[0].trim()),
                Integer.parseInt(rgbString[1].trim()),
                Integer.parseInt(rgbString[2].trim()));
    }
}
