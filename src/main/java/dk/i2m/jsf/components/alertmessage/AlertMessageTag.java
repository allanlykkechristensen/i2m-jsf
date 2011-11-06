/*
 * AlertMessageTag.java
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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 *
 * @author Allan Lykke Christensen
 */
public class AlertMessageTag extends UIComponentELTag {

    private static final String ATTRIBUTE_FOR = "for";

    private ValueExpression forParam;

    @Override
    public String getComponentType() {
        return AlertMessage.ALERT_MSG_COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return AlertMessage.ALERT_MSG_RENDERER_TYPE;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        AlertMessage em = (AlertMessage) component;
        em.setValueExpression(ATTRIBUTE_FOR, forParam);
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    @Override
    public void setPageContext(PageContext ctx) {
        super.setPageContext(ctx);
    }

    @Override
    public void setParent(Tag tag) {
        super.setParent(tag);
    }

    public void setFor(ValueExpression forParam) {
        this.forParam = forParam;
    }
}
