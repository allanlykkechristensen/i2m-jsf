/*
 * Copyright (C) 2008 - 2011 Interactive Media Management
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
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
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
 * @author <a href="mailto:allan@interactivemediamanagement.com">Allan Lykke Christensen</a>
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
     * Gets the {@link ResourceBundle} of the current JSF context.
     *
     * @return {@link ResourceBundle} of the current context
     */
    public static ResourceBundle getResourceBundle() {
        return getResourceBundle(FacesContext.getCurrentInstance());
    }

    /**
     * Gets the message bundle from a given JSF context.
     *
     * @param ctx
     *            {@link FacesContext} of the current application
     * @return {@link ResourceBundle} containing the message bundle of the 
     *         current application
     */
    public static ResourceBundle getResourceBundle(FacesContext ctx) {
        // Get the current JSF application
        Application application = ctx.getApplication();

        // Get the name of the message bundle used.
        String messageBundleName = application.getMessageBundle();

        // Get the locale of the current view
        Locale locale = ctx.getViewRoot().getLocale();

        // Load the resource bundle
        return ResourceBundle.getBundle(messageBundleName, locale);
    }

    /**
     * Gets the {@link ResourceBundle} from a given JSF context.
     *
     * @param ctx
     *          {@link FacesContext} of the current application
     * @param bundleId
     *          Unique identifier of the bundle to fetch
     * @return {@link ResourceBundle} of the current application
     */
    public static ResourceBundle getResourceBundle(FacesContext ctx, String bundleId) {
        // Get the current JSF application
        Application application = ctx.getApplication();

        // Get the resource bundle
        ResourceBundle bundle;
        try {
            bundle = application.getResourceBundle(ctx, bundleId);
        } catch (Exception ex) {
            ex.printStackTrace();
            bundle = getResourceBundle(ctx);
        }

        return bundle;
    }

    /**
     * Obtain the {@link Map} of data stored in the current session for the
     * current user.
     *
     * @param ctx {@link FacesContext} containing the session {@link Map}
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
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param param
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(FacesMessage.Severity severity,
            String messageKey, Object param) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle bundle = getResourceBundle(ctx);
        String msgPattern = bundle.getString(messageKey);
        String msg = msgPattern;

        if (param != null) {
            Object[] params = {param};
            msg = MessageFormat.format(msgPattern, params);
        }

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
     * @param severity
     *          Severity of the message
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param params
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(FacesMessage.Severity severity, String messageKey, Object[] params) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle bundle = getResourceBundle(ctx);
        String msgPattern = "";
        try {
            msgPattern = bundle.getString(messageKey);
        } catch (MissingResourceException ex) {
            msgPattern = messageKey;
        }
        String msg = msgPattern;

        msg = MessageFormat.format(msgPattern, params);

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
     * @param severity
     *          Severity of the message
     * @param useBundle
     *          If the resource bundle should be used to look up a message
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param param
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(FacesMessage.Severity severity,
            boolean useBundle, String messageKey, Object param) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        String msg = messageKey;
        if (useBundle) {
            ResourceBundle bundle = getResourceBundle(ctx);
            String msgPattern = bundle.getString(messageKey);
            msg = msgPattern;
        }

        if (param != null) {
            Object[] params = {param};
            msg = MessageFormat.format(msg, params);
        }

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
     * @param severity
     *          Severity of the message
     * @param bundleName 
     *          Name of the bundle from where the get the message
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param param
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(FacesMessage.Severity severity,
            String bundleName, String messageKey, Object param) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        String msg = messageKey;

        ResourceBundle bundle = getResourceBundle(ctx, bundleName);
        String msgPattern = bundle.getString(messageKey);
        msg = msgPattern;


        if (param != null) {
            Object[] params = {param};
            msg = MessageFormat.format(msg, params);
        }

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
     * @param severity
     *          Severity of the message
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(FacesMessage.Severity severity,
            String messageKey) {
        return createMessage(severity, messageKey, null);
    }

    /**
     * Generates a {@link FacesMessage} from an entry in the
     * {@link ResourceBundle} of the Faces application.
     *
     * @param componentId
     *          ID of the component to attach the message to
     * @param severity
     *          Severity of the message
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param param
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(String componentId,
            FacesMessage.Severity severity, String messageKey, Object param) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, messageKey, param);

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
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param params
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(String componentId,
            FacesMessage.Severity severity, String messageKey, Object[] params) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, messageKey, params);

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
     * @param useBundle
     *          If the resource bundle should be used to look up messages
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @param param
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(String componentId,
            FacesMessage.Severity severity, boolean useBundle, String messageKey,
            Object param) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, useBundle, messageKey, param);

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
     * @param param
     *          Parameters to go into the message
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(String componentId,
            FacesMessage.Severity severity, String bundleName, String messageKey,
            Object param) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, bundleName, messageKey, param);

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
     * @param messageKey
     *          Key of the message in the {@link ResourceBundle}
     * @return {@link FacesMessage} initialised with the given information
     */
    public static FacesMessage createMessage(String componentId, FacesMessage.Severity severity, String messageKey) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = createMessage(severity, messageKey);

        ctx.addMessage(componentId, msg);

        return msg;
    }

    /**
     * Gets a {@link ValueExpression} from the current {@link FacesContext}.
     *
     * @param name
     *          Name of the {@link ValueExpression} to obtain
     * @return {@link ValueExpression} matching the <code>name</code>
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
