package jsx3.xml;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class CdfDocumentTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testApp()
    {
        CdfDocument document = new CdfDocument("root");

        Record record = new Record("US").setAttribute("index", "1");
        document.appendRecord(record);

        String xml = document.toXml();

        assertTrue(xml.startsWith("<data jsxid=\"jsxroot\">"));
        assertTrue(xml.contains("<record jsxid=\"US\" index=\"1\"/>"));
        assertTrue(xml.contains("</data>"));
    }
}
