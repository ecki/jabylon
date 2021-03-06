/**
 * 
 */
package de.jutzig.jabylon.rest.ui.security;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.common.util.EList;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jutzig.jabylon.cdo.server.ServerConstants;
import de.jutzig.jabylon.rest.ui.Activator;
import de.jutzig.jabylon.rest.ui.model.EObjectModel;
import de.jutzig.jabylon.security.JabylonSecurityBundle;
import de.jutzig.jabylon.users.Permission;
import de.jutzig.jabylon.users.User;
import de.jutzig.jabylon.users.UserManagement;

/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 *
 */
public class CDOAuthenticatedSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;

	private IModel<User> user;
	
	private static final Logger logger = LoggerFactory.getLogger(CDOAuthenticatedSession.class);
	
	private static final String JAAS_CONFIG_FILE = "META-INF/jaas.config"; //$NON-NLS-1$
	
	public CDOAuthenticatedSession(Request request) {
		super(request);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.authroles.authentication.AuthenticatedWebSession#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(final String username, final String password) {

		String configName = "DB"; //$NON-NLS-1$
		URL configUrl = JabylonSecurityBundle.getBundleContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		final ILoginContext loginContext = LoginContextFactory.createContext(configName, configUrl, new CallbackHandler() {
			
			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
				for (int i = 0; i < callbacks.length; i++) {
					Callback cb = callbacks[i];
					if(cb instanceof NameCallback) {
						((NameCallback) cb).setName(username); //$NON-NLS-1$
					} else if (cb instanceof PasswordCallback) {
						((PasswordCallback) cb).setPassword(password.toCharArray()); //$NON-NLS-1$
					}
				}
			}
		});
		try {
			loginContext.login();
			logger.info("Login for user {} successfull",username);
			//TODO: this could be handled without two lookups
			CDOView view = Activator.getDefault().getRepositoryConnector().openView();
			CDOResource resource = view.getResource(ServerConstants.USERS_RESOURCE);
			UserManagement userManagement = (UserManagement) resource.getContents().get(0);
			userManagement = Activator.getDefault().getRepositoryLookup().lookup(userManagement.cdoID());
			User user = userManagement.findUserByName(username);
			this.user = new EObjectModel<User>(user);
			view.close();
			
			return true;
		} catch (LoginException e) {
			logger.error("Login for user "+username+" failed",e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles() {
		// TODO Auto-generated method stub
		if(isSignedIn())
		{
			//in our case permissions are wicket roles
			EList<Permission> permissions = user.getObject().getAllPermissions();
			List<String> roleNames = new ArrayList<String>(permissions.size());
			for (Permission permission : permissions) {
				roleNames.add(permission.getName());
			}
			return new Roles(roleNames.toArray(new String[permissions.size()]));
		}
		return null;
	}

	public User getUser() {
		if(user == null)
			return null;
		return user.getObject();
	}
	
}
