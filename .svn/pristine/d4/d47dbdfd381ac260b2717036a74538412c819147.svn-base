package com.panelManagement.utils;

import org.w3c.dom.Element;

public class XMLParser {
    public static String getStringFromElement(Element element, int position,
                                              String key) {
        /*
		 * Return value
		 */
        try {
            return element.getElementsByTagName(key).item(position)
                    .getTextContent().trim().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
