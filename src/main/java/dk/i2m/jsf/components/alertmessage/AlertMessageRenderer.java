/*
 * AlertMessageRenderer.java
 *
 * Copyright (C) 2008 Interactive Media Management
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
package dk.i2m.jsf.components.alertmessage;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Allan Lykke Christensen
 */
public class AlertMessageRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AlertMessage alertMessage = (AlertMessage) component;

        Iterator iter;
        if (alertMessage.getFor() != null) {
            // Locate the component for which to display messages  
            UIComponent forComponent = alertMessage.findComponent(alertMessage.getFor());

            // If the component could not be found end processing  
            if (forComponent == null) {
                return;
            }

            iter = context.getMessages(forComponent.getClientId(context));
        } else {
            iter = context.getMessages();
        }

        // Iterate through messages for the component  

        if (iter.hasNext()) {
            ResponseWriter writer = context.getResponseWriter();

            // Start the script tag  
            writer.startElement("script", alertMessage);
            writer.writeAttribute("type", "text/javascript", null);

            // Construct one big string of all messages  
            StringBuffer message = new StringBuffer();
            while (iter.hasNext()) {
                FacesMessage msg = (FacesMessage) iter.next();
                if (message.length() > 0) {
                    // Separate each message with the JavaScript escape code  
                    // for newline  
                    message.append("\n");
                }

                message.append(msg.getDetail());
            }

            // Escape the constructed string to be outputable as a JavaScript  
            String displayMessage = StringEscapeUtils.escapeJavaScript(message.toString());

            // Output the javascript code for displaying the alert dialogue  
            String jsAlert = "alert('" + displayMessage + "');";
            writer.writeText(jsAlert.toCharArray(), 0, jsAlert.length());

            // End the script tag  
            writer.endElement("script");
        }
    }

    @Override
    public void decode(FacesContext ctx, UIComponent component) {
        if (ctx == null || component == null) {
            throw new NullPointerException();
        }
    }
}  
