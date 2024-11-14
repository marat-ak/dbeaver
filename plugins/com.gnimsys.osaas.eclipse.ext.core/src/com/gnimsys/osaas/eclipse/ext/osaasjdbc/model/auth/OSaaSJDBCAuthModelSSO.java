/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2024 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.auth;

import java.sql.SQLDataException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.impl.auth.AuthModelDatabaseNative;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.runtime.DBWorkbench;
import org.jkiss.dbeaver.ui.UIUtils;
import org.osgi.framework.Bundle;

/**
 * Oracle database native auth model.
 */
class JWT {
	String token, principal, type, error;
	double expirationIn;

	public JWT(String error) {
		this.error = error;

	}

	public JWT(String token, String principal, String type, double exp) {
		this.expirationIn = exp;
		this.token = token;
		this.principal = principal;
		this.type = type;
		this.error = null;
	}
}

public class OSaaSJDBCAuthModelSSO extends AuthModelDatabaseNative<OSaaSJDBCAuthModelSSOCredentials> {

	public static final String ID = "osaasjdbc_sso";

	@NotNull
	@Override
	public OSaaSJDBCAuthModelSSOCredentials createCredentials() {
		return new OSaaSJDBCAuthModelSSOCredentials();
	}

	@NotNull
	@Override
	public OSaaSJDBCAuthModelSSOCredentials loadCredentials(@NotNull DBPDataSourceContainer dataSource,
			@NotNull DBPConnectionConfiguration configuration) {
		OSaaSJDBCAuthModelSSOCredentials credentials = super.loadCredentials(dataSource, configuration);
		credentials.setUserName(null);
		credentials.setUserPassword(null);
		return credentials;
	}

	@Override
	public Object initAuthentication(@NotNull DBRProgressMonitor monitor, @NotNull DBPDataSource dataSource,
			@NotNull OSaaSJDBCAuthModelSSOCredentials credentials, @NotNull DBPConnectionConfiguration configuration,
			@NotNull Properties connProperties) throws DBException {
		// connProperties.put("v$session.osuser",
		// System.getProperty(StandardConstants.ENV_USER_NAME));
		/*
		Bundle bundle = Platform.getBundle("com.gnimsys.osaas.jdbc.eclipse.feature");
		String bundleLoc = "Not known";
		if(bundle != null) {
			bundleLoc = bundle.getLocation();
			
		}
		System.out.println(bundleLoc);
		Display display =  UIUtils.getDisplay();;//new Display();
		Shell shell = new Shell(display);

		
		shell.setLayout(new FillLayout());
		final String hostName = configuration.getHostName(), podUrl = "https://" + configuration.getHostName();
		shell.setText(hostName);
		Browser browser = new Browser(shell, SWT.NONE);
		AtomicReference<String> jwt = new AtomicReference<>();
		final String jScript = """
				(async function(){
				  try{
				   let resp = await fetch('/fscmRestApi/anticsrf', {
				          method: 'GET',
				          mode: 'cors',
				          cache: 'no-cache',
				          credentials: 'same-origin',
				            });
				         const anticsrf = await resp.json();
				         resp = await fetch('/fscmRestApi/tokenrelay', {
				          method: 'GET',
				          mode: 'cors',
				          cache: 'no-cache',
				          credentials: 'same-origin',
				          headers: {
				              'X-XSRF-TOKEN': anticsrf['xsrftoken']
				          }
				          });
				          const token= await resp.json();
				          //handleAsyncResult(JSON.stringify(token));
				          handleAsyncResult(null,token['access_token'],token['principal'],token['expires_in'],token['token_type']);
				      }
				      catch (error) {
				             alert(error);
				             handleAsyncResult(error);
				           }
				})();
				      
				      """;
		browser.addProgressListener(new org.eclipse.swt.browser.ProgressListener() {
			@Override
			public void completed(org.eclipse.swt.browser.ProgressEvent event) {
				// Execute async JavaScript after the page has loaded

				String loc = browser.getUrl(), locCompare = loc.toLowerCase();
				if ((locCompare.startsWith(podUrl.toLowerCase() + "/fscmUI/faces/FuseWelcome".toLowerCase())
						|| locCompare
								.startsWith(podUrl.toLowerCase() + "/fscmUI/faces/AtkHomePageWelcome".toLowerCase()))) {
					browser.evaluate(jScript);
				}
			}

			@Override
			public void changed(org.eclipse.swt.browser.ProgressEvent event) {
				// This method is called to notify progress
			}
		});
		browser.setUrl(podUrl);
		CompletableFuture<JWT> completableFuture = new CompletableFuture<>();
		new BrowserFunction(browser, "handleAsyncResult") {
			@Override
			public Object function(Object[] arguments) {
				System.out.println("Async result: " + arguments[0]);
				JWT res = null;
				if (arguments.length == 1) {
					res = new JWT((String) arguments[0]);
				} else {
					res = new JWT((String) arguments[1], (String) arguments[2], (String) arguments[4],
							(double) arguments[3]);
				}
				completableFuture.complete(res);
				return null;
			}
		};
		shell.open();
		shell.setActive();
		
		while (!shell.isDisposed() && !completableFuture.isDone()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		try {
			JWT result = completableFuture.get();
			if(result.error !=null) {
				throw new DBException(result.error);
			}
			connProperties.put("jwtPassword","true");
			connProperties.put("password",result.token);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DBException(e.getMessage());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new DBException(e.getMessage());
		} finally {
			display.dispose();
		}*/
		connProperties.put("sso","true");
		connProperties.put("isDBeaver","true");
		return super.initAuthentication(monitor, dataSource, credentials, configuration, connProperties);
	}

}
