/**
 * 
 */
package de.jutzig.jabylon.rest.ui.tools;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.jutzig.jabylon.rest.ui.model.PropertyPair;

/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 *
 */
public class PropertyToolTab implements ITab {

	private static final long serialVersionUID = 1L;
	private PropertyEditorTool tool;
	private IModel<PropertyPair> model;
	
	

	public PropertyToolTab(PropertyEditorTool tool) {
		super();
		this.tool = tool;
		model = new Model<PropertyPair>();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.tabs.ITab#getTitle()
	 */
	@Override
	public IModel<String> getTitle() {
		return Model.of(tool.getName());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.tabs.ITab#getPanel(java.lang.String)
	 */
	@Override
	public WebMarkupContainer getPanel(String containerId) {
		return tool.createPanel(new PageParameters(), model, containerId);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.tabs.ITab#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return true;
	}
	
	public void setModel(IModel<PropertyPair> model) {
		this.model.setObject(model.getObject());
		
	}
	
	

}
