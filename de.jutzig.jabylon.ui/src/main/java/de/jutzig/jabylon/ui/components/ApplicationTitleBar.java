package de.jutzig.jabylon.ui.components;

import java.text.MessageFormat;
import java.util.Set;

import javax.security.auth.Subject;

import com.vaadin.Application.UserChangeEvent;
import com.vaadin.Application.UserChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;

import de.jutzig.jabylon.ui.applications.MainDashboard;
import de.jutzig.jabylon.ui.breadcrumb.BreadCrumb;
import de.jutzig.jabylon.ui.breadcrumb.CrumbListener;
import de.jutzig.jabylon.ui.breadcrumb.CrumbTrail;
import de.jutzig.jabylon.ui.config.internal.DynamicConfigUtil;
import de.jutzig.jabylon.ui.resources.ImageConstants;
import de.jutzig.jabylon.ui.styles.JabylonStyle;
import de.jutzig.jabylon.users.User;

public class ApplicationTitleBar extends CustomComponent implements CrumbListener, UserChangeListener{

	private HorizontalLayout mainLayout;
	private Button settings;
	private Button login;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ApplicationTitleBar() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
	}

	private HorizontalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new HorizontalLayout();
		mainLayout.setWidth(100, UNITS_PERCENTAGE);
		mainLayout.setImmediate(false);
		mainLayout.setMargin(false);
		mainLayout.setSpacing(true);
		mainLayout.setStyleName(JabylonStyle.BREADCRUMB_PANEL.getCSSName());
		// top-level component properties

        // Upper left logo
		Embedded title = new Embedded(null, ImageConstants.IMAGE_LOGO);

		title.setHeight(44,Embedded.UNITS_PIXELS);
//		title.setCaption("Jabylon");
		title.setWidth(124, Label.UNITS_PIXELS);
//		title.setStyleName(Reindeer.LABEL_H1);
		title.setStyleName(JabylonStyle.APPLICATION_TITLE.getCSSName());
		mainLayout.addComponent(title);
		mainLayout.setComponentAlignment(title, Alignment.TOP_LEFT);

		Button help = new Button(Messages.getString("ApplicationTitleBar_HELP_BUTTON_CAPTION")); //$NON-NLS-1$
		help.setIcon(ImageConstants.IMAGE_HELP);
		help.setStyleName(Reindeer.BUTTON_LINK);
		mainLayout.addComponent(help);
		mainLayout.setComponentAlignment(help, Alignment.BOTTOM_RIGHT);
		mainLayout.setExpandRatio(help, 2f);

		settings = new Button(Messages.getString("ApplicationTitleBar_SETTINGS_BUTTON_CAPTION")); //$NON-NLS-1$
		settings.setIcon(ImageConstants.IMAGE_SETTINGS);
		settings.addListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				settingsPressed();

			}
		});
		settings.setStyleName(Reindeer.BUTTON_LINK);
		mainLayout.addComponent(settings);
		mainLayout.setComponentAlignment(settings, Alignment.BOTTOM_RIGHT);


		login = new Button(Messages.getString("ApplicationTitleBar_LOGIN_BUTTON_CAPTION")); //$NON-NLS-1$
		login.setStyleName(Reindeer.BUTTON_LINK);
		login.setIcon(ImageConstants.IMAGE_LOGIN);
		login.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(MainDashboard.getCurrent().getUser()!=null) {
					MainDashboard.getCurrent().setUser(null);
				} else {
					LoginDialog loginDialog = new LoginDialog(MainDashboard.getCurrent());
					loginDialog.display();
				}
			}
		});
		mainLayout.addComponent(login);
		mainLayout.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);


		return mainLayout;
	}

	protected void settingsPressed() {
		MainDashboard.getCurrent().getBreadcrumbs().walkTo(BreadCrumb.CONFIG);

	}

	@Override
	public void activeCrumbTrailChanged(CrumbTrail current) {
		settings.setVisible(!DynamicConfigUtil.getApplicableElements(current.getDomainObject()).isEmpty());
	}

	@Override
	public void applicationUserChanged(UserChangeEvent event) {
		Object newUser = event.getNewUser();
		if(newUser==null) {
			login.setCaption(Messages.getString("ApplicationTitleBar_LOGIN_BUTTON_CAPTION")); //$NON-NLS-1$
		}

		if (newUser instanceof Subject) {
			Subject user = (Subject) newUser;
			Set<String> publicCredentials = user.getPublicCredentials(String.class);
			if(publicCredentials.size()==1) {
				login.setCaption("Logout <"+publicCredentials.iterator().next()+">"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else
				;// TODO error handling
		}

		if (newUser instanceof User) {
			User user = (User) newUser;
			String message = Messages.getString("ApplicationTitleBar_LOGOUT_BUTTON_CAPTION"); //$NON-NLS-1$
			login.setCaption(MessageFormat.format(message, user.getName()));
		}
		CrumbTrail current = MainDashboard.getCurrent().getBreadcrumbs().currentTrail();
		settings.setVisible(!DynamicConfigUtil.getApplicableElements(current.getDomainObject()).isEmpty());

	}

}
