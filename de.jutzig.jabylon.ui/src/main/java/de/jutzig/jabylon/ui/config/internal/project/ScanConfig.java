/**
 *
 */
package de.jutzig.jabylon.ui.config.internal.project;

import org.osgi.service.prefs.Preferences;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.jutzig.jabylon.properties.Project;
import de.jutzig.jabylon.properties.PropertiesFactory;
import de.jutzig.jabylon.properties.PropertiesPackage;
import de.jutzig.jabylon.properties.ScanConfiguration;
import de.jutzig.jabylon.ui.config.AbstractConfigSection;
import de.jutzig.jabylon.ui.container.PreferencesItem;
import de.jutzig.jabylon.ui.resources.ImageConstants;
import de.jutzig.jabylon.ui.util.PreferencesUtil;

/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 *
 */
@SuppressWarnings("serial")
public class ScanConfig extends AbstractConfigSection<Project> {
	private Form form;

	/* (non-Javadoc)
	 * @see de.jutzig.jabylon.ui.config.ConfigSection#createContents()
	 */
	@Override
	public Component createContents() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		form = createForm();
		layout.addComponent(form);

		Button scanProject = new Button();
		scanProject.setCaption("Full Scan");
		scanProject.setIcon(ImageConstants.IMAGE_PROJECT_SCAN);
		scanProject.addListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				ScanConfiguration configuration = PropertiesFactory.eINSTANCE.createScanConfiguration();
				configuration.setInclude((String) form.getField(PreferencesUtil.SCAN_CONFIG_INCLUDE).getValue());
				configuration.setExclude((String) form.getField(PreferencesUtil.SCAN_CONFIG_EXCLUDE).getValue());
				configuration.setMasterLocale((String) form.getField(PreferencesUtil.SCAN_CONFIG_MASTER_LOCALE).getValue());
				getDomainObject().fullScan(configuration);
				form.getWindow().showNotification("Scan complete");
			}

		});
		layout.addComponent(scanProject);
		return layout;
	}

	private Form createForm() {
		Form form = new Form();
		form.setWriteThrough(true);
		form.setImmediate(true);
		form.setFormFieldFactory(new DefaultFieldFactory() {


			@Override
			public Field createField(Item item, Object propertyId, Component uiContext) {
				if(propertyId==PreferencesUtil.SCAN_CONFIG_INCLUDE)
				{
					TextArea area = new TextArea();
					area.setCaption(createCaptionByPropertyId(propertyId));
					area.setDescription("ANT Fileset Pattern. Seperate includes with a linefeed");
					area.setRows(3);
					area.setColumns(30);
					area.setNullRepresentation("");
					return area;
				}
				if(propertyId==PreferencesUtil.SCAN_CONFIG_EXCLUDE)
				{
					TextArea area = new TextArea();
					area.setCaption(createCaptionByPropertyId(propertyId));
					area.setDescription("ANT Fileset Pattern. Seperate excludes with a linefeed");
					area.setRows(3);
					area.setColumns(30);
					area.setNullRepresentation("");
					return area;
				}
				if(propertyId==PreferencesUtil.SCAN_CONFIG_MASTER_LOCALE)
				{
					TextField field = new TextField();
					field.setCaption("Master Locale");
					field.setColumns(30);
					field.setNullRepresentation("");
					field.setDescription("Leave empty if you follow the standard java pattern of 'messages.properties'");
					return field;
				}
				return super.createField(item, propertyId, uiContext);
			}
		});
		return form;
	}

	/* (non-Javadoc)
	 * @see de.jutzig.jabylon.ui.config.ConfigSection#commit(org.osgi.service.prefs.Preferences)
	 */
	@Override
	public void commit(Preferences config) {
		form.commit();

	}

	/* (non-Javadoc)
	 * @see de.jutzig.jabylon.ui.config.AbstractConfigSection#init(org.osgi.service.prefs.Preferences)
	 */
	@Override
	protected void init(Preferences config) {
		PreferencesItem item = new PreferencesItem(config);
		item.addProperty(PreferencesUtil.SCAN_CONFIG_INCLUDE, String.class, PropertiesPackage.Literals.SCAN_CONFIGURATION__INCLUDE.getDefaultValueLiteral());
		item.addProperty(PreferencesUtil.SCAN_CONFIG_EXCLUDE, String.class, PropertiesPackage.Literals.SCAN_CONFIGURATION__EXCLUDE.getDefaultValueLiteral());
		item.addProperty(PreferencesUtil.SCAN_CONFIG_MASTER_LOCALE, String.class, PropertiesPackage.Literals.SCAN_CONFIGURATION__MASTER_LOCALE.getDefaultValueLiteral());
		form.setItemDataSource(item);

	}

}
