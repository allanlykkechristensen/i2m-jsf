/*
 * StringLineBreakConverter.java
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converter for replacing system line breaks with HTML break lines
 * (<code>&lt;br /&gt;</code>).
 *
 * @author Allan Lykke Christensen
 */
public class StringLineBreakConverter implements Converter {

    private static final String LINE_BREAK = System.getProperty("line.separator");

    /**
     * Converts HTML break lines to system line break.
     *
     * @param ctx
     *          Facelets context
     * @param comp
     *          Component using the converter
     * @param value
     *          Value to conver
     * @return {@link String} with HTML break lines replaced with system line
     *         breaks
     * @throws javax.faces.convert.ConverterException
     *          If the convertion failed
     */
    @Override
    public Object getAsObject(FacesContext ctx, UIComponent comp, String value)
            throws ConverterException {
        String html = value.replaceAll("<br/>", LINE_BREAK);
        return html.replaceAll("<br>", LINE_BREAK);
    }

    /**
     * Converts the {@link String} with line break to a {@link String} with HTML
     * break lines.
     *
     * @param ctx
     *          Facelets context
     * @param comp
     *          Component using the converter
     * @param obj
     *          Object to convert
     * @return String with HTML break lines
     * @throws javax.faces.convert.ConverterException
     *          If the convertion failed
     */
    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, Object obj)
            throws ConverterException {
        String plain = (String) obj;
        return plain.replaceAll(LINE_BREAK, "<br/>");
    }
}
