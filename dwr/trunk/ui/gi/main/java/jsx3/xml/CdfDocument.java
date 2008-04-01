package jsx3.xml;

/**
 * CdfDocuments and the root of a Node tree.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class CdfDocument extends Node
{
	/**
	 * Ensure all Records have unique IDs
	 * @param id the jsxid for this record 
	 */
	public CdfDocument(String id)
	{
		super(id);
	}

	/**
	 * Generate an XML string suitable for marshalling
	 */
	public String toXml()
	{
		StringBuilder result = new StringBuilder();

        // result.append("<?xml version=\"1.0\"?>\n");
        // result.append("\n");

        result.append("<data jsxid=\"jsxroot\">\n");

		for (Record record : this)
        {
            result.append(record.toXml(0));
            result.append("\n");
		}

        result.append("</data>\n");

        return result.toString();
	}
}
