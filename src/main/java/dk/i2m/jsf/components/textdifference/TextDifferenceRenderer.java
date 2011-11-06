/*
 *  Copyright (C) 2010 - 2011 Interactive Media Management
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.jsf.components.textdifference;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

/**
 * {@link Renderer} for the {@link TextDifference} {@link UIComponent}.
 *
 * @author Allan Lykke Christensen
 */
public class TextDifferenceRenderer extends Renderer {

    @Override
    public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
        super.encodeBegin(facesContext, component);
        Map<String, Object> attributes = component.getAttributes();
        String oldText = (String) attributes.get(TextDifferenceTag.ATTRIBUTE_OLD_TEXT);
        String newText = (String) attributes.get(TextDifferenceTag.ATTRIBUTE_NEW_TEXT);
        String styleDeleted = (String) attributes.get(TextDifferenceTag.ATTRIBUTE_STYLE_DELETED);
        String styleInserted = (String) attributes.get(TextDifferenceTag.ATTRIBUTE_STYLE_INSERTED);
        String styleEqual = (String) attributes.get(TextDifferenceTag.ATTRIBUTE_STYLE_EQUAL);
        String style = (String) attributes.get(TextDifferenceTag.ATTRIBUTE_STYLE);

        final ResponseWriter writer = facesContext.getResponseWriter();

        diff_match_patch diff = new diff_match_patch();
        LinkedList<Diff> differences = diff.diff_main(stripHtml(oldText), stripHtml(newText));
        String output = prettyHtml(differences, styleInserted, styleDeleted, styleEqual);

        writer.startElement("div", component);
        writer.writeAttribute("class", style, "class");
        writer.write(output);
        writer.endElement("div");
    }

    private static String prettyHtml(LinkedList<Diff> diffs, String styleInserted, String styleDeleted, String styleEqual) {
        StringBuilder html = new StringBuilder();
        for (Diff aDiff : diffs) {
            String text = aDiff.text;
            switch (aDiff.operation) {
                case INSERT:
                    html.append("<span class=\"").append(styleInserted).append("\">").append(text).append("</span>");
                    break;
                case DELETE:
                    html.append("<span class=\"").append(styleDeleted).append("\">").append(text).append("</span>");
                    break;
                case EQUAL:
                    html.append("<span class=\"").append(styleEqual).append("\">").append(text).append("</span>");
                    break;
            }
        }
        return html.toString();
    }

    /**
     * Strips HTML tags from a {@link String}.
     *
     * @param htmlString
     *          {@link String} containing HTML tags
     * @return String with HTML tags stripped
     */
    private static String stripHtml(final String htmlString) {
        StringBuilder sb = new StringBuilder("");
        boolean intag = false;
        for (int i = 0; i < htmlString.length(); i++) {
            char c = htmlString.charAt(i);
            switch (c) {
                case '<':
                    intag = true;
                    break;
                case '>':
                    intag = false;
                    break;
                default:
                    if (!intag) {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }
}
