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

        assertEquals(xml, "");
    }
}
/*
<data jsxid="jsxroot">
<record index="1" jsxid="US" jsxtext="United States"/>
<record index="2" jsxid="UK" jsxtext="United Kingdom"/>
<record index="3" jsxid="AG" jsxtext="Afghanistan"/>
<record index="4" jsxid="AL" jsxtext="Albania"/>
<record index="5" jsxid="AR" jsxtext="Algeria"/>
<record index="6" jsxid="AS" jsxtext="American Samoa"/>
<record index="7" jsxid="AD" jsxtext="Andorra"/>
<record index="8" jsxid="AO" jsxtext="Angola"/>
<record index="9" jsxid="AU" jsxtext="Anguilla"/>
</data>
*/
