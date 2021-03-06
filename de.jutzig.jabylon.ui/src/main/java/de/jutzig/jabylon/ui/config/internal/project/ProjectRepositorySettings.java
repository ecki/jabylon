/**
 *
 */
package de.jutzig.jabylon.ui.config.internal.project;

import java.util.Arrays;

import org.eclipse.core.runtime.IConfigurationElement;
import org.osgi.service.prefs.Preferences;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

import de.jutzig.jabylon.properties.Project;
import de.jutzig.jabylon.properties.PropertiesPackage;
import de.jutzig.jabylon.ui.Activator;
import de.jutzig.jabylon.ui.config.AbstractConfigSection;
import de.jutzig.jabylon.ui.config.ConfigSection;
import de.jutzig.jabylon.ui.container.EObjectItem;

/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 *
 */
@SuppressWarnings("serial")
public class ProjectRepositorySettings extends AbstractConfigSection<Project> implements ConfigSection {

	private Form form;

	/**
	 *
	 */
	public ProjectRepositorySettings() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.jutzig.jabylon.ui.config.ConfigSection#createContents()
	 */
	@Override
	public Component createContents() {
		form = new Form();
		form.setWriteThrough(true);
		form.setValidationVisible(true);
		form.setImmediate(true);
		form.setFormFieldFactory(new DefaultFieldFactory() {

			@Override
			public Field createField(Item item, Object propertyId, Component uiContext) {
				if (propertyId == PropertiesPackage.Literals.PROJECT__TEAM_PROVIDER)
				{
					NativeSelect select = new NativeSelect(Messages.getString("ProjectRepositorySettings_SELECT_TEAM_PROVIDER_CAPTION"));
					IConfigurationElement[] teamProviders = Activator.getDefault().getTeamProviders();
					for (IConfigurationElement iConfigurationElement : teamProviders) {
						String name = iConfigurationElement.getAttribute("name"); //$NON-NLS-1$
						select.addItem(name);
					}
					
					select.setNullSelectionAllowed(true);
					return select;
				}
				Field field = super.createField(item, propertyId, uiContext);
				if(propertyId == PropertiesPackage.Literals.PROJECT__REPOSITORY_URI)
				{
					((TextField)field).setInputPrompt(Messages.getString("ProjectRepositorySettings_REPOSITORY_URI_TEXTFIELD_PROMPT"));
					field.setCaption(Messages.getString("ProjectRepositorySettings_REPOSITORY_URI_TEXTFIELD_CAPTION"));
				}
				if (field instanceof TextField) {
					TextField text = (TextField) field;
					text.setNullRepresentation(""); //$NON-NLS-1$
					text.setColumns(30);
				}

				return field;
			}

		});
		return form;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.jutzig.jabylon.ui.config.ConfigSection#commit(org.osgi.service.prefs
	 * .Preferences)
	 */
	@Override
	public void commit(Preferences config) {


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.jutzig.jabylon.ui.config.AbstractConfigSection#init(org.osgi.service
	 * .prefs.Preferences)
	 */
	@Override
	protected void init(Preferences config) {
		EObjectItem projectBean = new EObjectItem(getDomainObject());	
		form.setItemDataSource(projectBean);
		form.setVisibleItemProperties(Arrays.asList(PropertiesPackage.Literals.PROJECT__REPOSITORY_URI, PropertiesPackage.Literals.PROJECT__TEAM_PROVIDER));

	}


}
