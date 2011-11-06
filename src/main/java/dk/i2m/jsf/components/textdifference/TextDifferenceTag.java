/*
 *  Copyright (C) 2010 Interactive Media Management
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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * JSP {@link Tag} for the {@link TextDifference} {@link UIComponent}.
 *
 * @author Allan Lykke Christensen
 */
public class TextDifferenceTag extends UIComponentELTag {

    public static final String ATTRIBUTE_OLD_TEXT = "oldText";
    public static final String ATTRIBUTE_NEW_TEXT = "newText";
    public static final String ATTRIBUTE_STYLE_INSERTED = "styleInserted";
    public static final String ATTRIBUTE_STYLE_DELETED = "styleDeleted";
    public static final String ATTRIBUTE_STYLE_EQUAL = "styleEqual";
    public static final String ATTRIBUTE_STYLE = "styleClass";
    private ValueExpression oldTextParam;
    private ValueExpression newTextParam;
    private ValueExpression styleInsertedParam;
    private ValueExpression styleDeletedParam;
    private ValueExpression styleEqualParam;
    private ValueExpression styleParam;

    @Override
    public String getComponentType() {
        return TextDifference.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return TextDifference.RENDERER_TYPE;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        processProperty(component, oldTextParam, TextDifferenceTag.ATTRIBUTE_OLD_TEXT);
        processProperty(component, newTextParam, TextDifferenceTag.ATTRIBUTE_NEW_TEXT);
        processProperty(component, styleDeletedParam, TextDifferenceTag.ATTRIBUTE_STYLE_DELETED);
        processProperty(component, styleInsertedParam, TextDifferenceTag.ATTRIBUTE_STYLE_INSERTED);
        processProperty(component, styleEqualParam, TextDifferenceTag.ATTRIBUTE_STYLE_EQUAL);
        processProperty(component, styleParam, TextDifferenceTag.ATTRIBUTE_STYLE);
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

    public void setOldText(ValueExpression oldText) {
        this.oldTextParam = oldText;
    }

    public void setNewText(ValueExpression newText) {
        this.newTextParam = newText;
    }

    public void setStyleInserted(ValueExpression ve) {
        this.styleInsertedParam = ve;
    }

    public void setStyleDeleted(ValueExpression ve) {
        this.styleDeletedParam = ve;
    }

    public void setStyleEqual(ValueExpression ve) {
        this.styleEqualParam = ve;
    }

    public void setStyle(ValueExpression ve) {
        this.styleParam = ve;
    }

    @Override
    public void release() {
        super.release();
        this.newTextParam = null;
        this.oldTextParam = null;
        this.styleDeletedParam = null;
        this.styleEqualParam = null;
        this.styleInsertedParam = null;
        this.styleParam = null;
    }

    protected final void processProperty(final UIComponent component, final ValueExpression property, final String propertyName) {
        if (property != null) {
            if (property.isLiteralText()) {
                component.getAttributes().put(propertyName, property.getExpressionString());
            } else {
                component.setValueExpression(propertyName, property);
            }
        }
    }
}
