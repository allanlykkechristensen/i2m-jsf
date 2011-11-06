/*
 * AbbreviationDisplayConverter.java
 * 
 * Copyright (C) 2009 Interactive Media Management
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.jsf.converters;

import java.util.Map;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.lang.StringUtils;

/**
 * JSF {@link Converter} for abbreviating a {@link String}. The abbreviation is
 * a one-way conversion where only the
 * {@link Converter#getAsString(FacesContext, UIComponent, Object)} is
 * supported.<br/>
 * <br/>
 * <b>Example</b>
 * <code>
 * &lt;h:outputText value="A long long text"&gt;
 *     &lt;f:converter converterId="i2m.abbreviationDisplay" /&gt;
 *     &lt;f:attribute name="maxLength" value="5" /&gt;
 * &lt;/h:outputText&gt;
 * </code>
 *
 * @author Allan Lykke Christensen
 */
public class AbbreviationDisplayConverter implements Converter {

    private static Logger logger = Logger.getLogger(
            AbbreviationDisplayConverter.class.getName());

    private static final String MAX_LENGTH = "maxLength";

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        return null;
    }

    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
        if (value instanceof String) {
            String originalValue = (String) value;
            Map<String, Object> attrs = comp.getAttributes();
            if (attrs.containsKey(MAX_LENGTH)) {
                Object objMaxLength = attrs.get(MAX_LENGTH);
                if (objMaxLength instanceof String) {
                    String maxLength = (String) objMaxLength;

                    try {
                        return StringUtils.abbreviate(originalValue,
                                Integer.valueOf(maxLength));
                    } catch (NumberFormatException ex) {
                        logger.warning("Attribute '" + MAX_LENGTH +
                                "' must contain a String with a numeric value");
                        return originalValue;
                    }
                } else {
                    logger.warning("Attribute '" + MAX_LENGTH +
                            "' must contain a String with a numeric value");
                    return originalValue;
                }
            } else {
                logger.warning("Attribute '" + MAX_LENGTH + "' is missing");
                return originalValue;
            }
        } else {
            logger.warning("Value provided to " + getClass().getName() +
                    " was not a String but a " + value.getClass());
            return "";
        }
    }
}
