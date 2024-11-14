package com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.auth;

import org.jkiss.dbeaver.model.impl.auth.AuthModelDatabaseNativeCredentials;

public class OSaaSJDBCAuthModelSSOCredentials extends AuthModelDatabaseNativeCredentials {
	 @Override
	    public String getUserName() {
	        return super.getUserName();
	    }

	    @Override
	    public String getUserPassword() {
	        return super.getUserPassword();
	    }

}
