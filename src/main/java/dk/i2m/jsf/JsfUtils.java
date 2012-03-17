/*
 * Copyright (C) 2008 - 2012 Interactive Media Management
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
package dk.i2m.jsf;

import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * JavaServer Faces utility class.
 *
 * @author <a href="mailto:allan@i2m.dk">Allan Lykke Christensen</a>
 */
public class JsfUtils {

    /**
     * Gets the {@link HttpServletResponse} from a given {@link FacesContext}.
     * 
     * @param ctx 
     *          {@link FacesContext} from which to obtain the
     *          {@link HttpServletResponse}
     * @return {@link HttpServletResponse} from a given {@link FacesContext}
     */
    public static HttpServletResponse getHttpServletResponse(FacesContext ctx) {
        return (HttpServletResponse) ctx.getExternalContext().getResponse();
    }

    /**
     * Gets the {@link ResourceBundle} from a given JSF context.
     * 
     * @param bundleId 
     *          Unique identifier of the bundle to fetch
     * @return {@link ResourceBundle} of the current application
     * @since 1.4
     */
    public static ResourceBundle getResourceBundle(String bundleId) {
        // Get the current JSF application
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application application = ctx.getApplication();

        // Get the resource bundle
        ResourceBundle bundle;
        try {
            bundle = application.getResourceBundle(ctx, bundleId);
        } catch (Exception ex) {
            bundle = ResourceBundle.getBundle(application.getMessageBundle());
        }

        return bundle;
    }

    /**
     * Gets a message from a given {@link ResourceBundle} formatted using the
     * given parameters.
     * 
     * @param bundleId 
     *          Unique identifier of the {@ink ResourceBundle}
     * @param label    
     *          Label to fetch from the bundle
     * @param params   
     *          Replacement values for the label
     * @return Formatted message, or the value of {@code label} if it wasn't
     *         found in the bundle
     * @since 1.4
     */
    public static String getMessage(String bundleId, String label, Object[] params) {
        ResourceBundle bundle = getResourceBundle(bundleId);

        String msg;
        if (!bundle.containsKey(label)) {
            msg = label;
        } else {
            msg = bundle.getString(label);
        }

        msg = MessageFormat.format(msg, params);

        return msg;
    }

    /**
     * Obtain the {@link Map} of data stored in the current session for the
     * current user.
     *
     * @param ctx 
     *          {@link FacesContext} containing the session {@link Map}
     * @return {@link Map} of the data stored in the current session for the
     *         current user.
     */
    public static Map<String, Object> getSessionMap(FacesContext ctx) {
        @SuppressWarnings("unchecked")
        Map<String, Object> session = ctx.getExternalContext().getSessionMap();
        return session;
    }

    /**
     * Obtain the {@link Map} of data stored in the current session for the
     * current user.
     * 
     * @return {@link Map} of the data stored in the current session for the
     *         current user.
     */
    public static Map<String, Object> getSessionMap() {
        return getSessionMap(FacesContext.getCurrentInstance());
    }

    /**
     * Obtain the request parameter map in the external content.
     * 
     * @return {@link Map} of requests in the external content
     */
    public static Map<String, String> getRequestParameterMap() {
        ExternalContext ctx = FacesContext.getCurrentInstance().
                getExternalContext();
        return ctx.getRequestParameterMap();
    }

    /**
     * Generates a {@link FacesMessage} from an entry in the
     * {@link ResourceBundle} of the Faces application.
     * 
     * @param severity   
     *          Severity of the message
     * @param bundleName 
     *          Name of the bundle from where the get the message
     * @param messageKey 
     *          Key of the message in the {@link ResourceBundle}
     * @param params     
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(FacesMessage.Severity severity,
            String bundleName, String messageKey, Object[] params) {
        ResourceBundle bundle = getResourceBundle(bundleName);
        String msgPattern;
        if (bundle.containsKey(messageKey)) {
            msgPattern = bundle.getString(messageKey);
        } else {
            msgPattern = messageKey;
        }

        String msg = MessageFormat.format(msgPattern, params);

        FacesMessage facesMsg = new FacesMessage();
        facesMsg.setSeverity(severity);
        facesMsg.setSummary(msg);
        facesMsg.setDetail(msg);

        return facesMsg;
    }

    /**
     * Generates a {@link FacesMessage} from an entry in the
     * {@link ResourceBundle} of the Faces application.
     * 
     * @param componentId 
     *          ID of the component to attach the message to
     * @param severity    
     *          Severity of the message
     * @param bundleName 
     *          Name of the resource bundle containing the message
     * @param messageKey  
     *          Key of the message in the {@link ResourceBundle}
     * @param params      
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     * @since 1.4
     */
    public static FacesMessage createMessage(String componentId,
            FacesMessage.Severity severity, String bundleName, String messageKey,
            Object[] params) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, bundleName, messageKey, params);

        ctx.addMessage(componentId, msg);

        return msg;
    }

    /**
     * Generates a {@link FacesMessage} from an entry in the
     * {@link ResourceBundle} of the Faces application.
     *
     * @param componentId 
     *          ID of the component to attach the message to
     * @param severity    
     *          Severity of the message
     * @param bundleName  
     *          Name of the resource bundle containing the message
     * @param messageKey  
     *          Key of the message in the {@link ResourceBundle}
     * @return {@link FacesMessage} initialised with the given information
     * @since 1.4
     */
    public static FacesMessage createMessage(String componentId,
            FacesMessage.Severity severity, String bundleName, String messageKey) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, bundleName, messageKey, new Object[]{});

        ctx.addMessage(componentId, msg);

        return msg;
    }

    /**
     * Gets a {@link ValueExpression} from the current {@link FacesContext}.
     *
     * @param name 
     *          Name of the {@link ValueExpression} to obtain
     * @return {@link ValueExpression} matching the {@code name}
     */
    public static ValueExpression getValueExpression(String name) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = ctx.getELContext();
        return elFactory.createValueExpression(elContext, name, Object.class);
    }

    /**
     * Gets the value of a {@link ValueExpression}.
     *
     * @param name 
     *          Name of the {@link ValueExpression}
     * @return Value of the {@link ValueExpression}.
     */
    public static Object getValueOfValueExpression(String name) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = ctx.getELContext();
        ValueExpression ve = elFactory.createValueExpression(elContext, name,
                Object.class);
        return ve.getValue(elContext);
    }

    /**
     * Gets the {@link HttpSession} from the current {@link FacesContext}.
     *
     * @return {@link HttpSession} from the current {@link FacesContext}.
     */
    public static HttpSession getHttpSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        return (HttpSession) externalContext.getSession(true);
    }

    /**
     * Sets a {@link String} value of a {@link ValueExpression}.
     *
     * @param valueExpression 
     *          Value expression
     * @param value 
     *          {@link String} value to set in the value expression
     */
    public static void setValue(final String valueExpression, final String value) {
        ValueExpression ve = getValueExpression(valueExpression);
        ve.setValue(FacesContext.getCurrentInstance().getELContext(), value);
    }
}
