package com.gnimsys.osaas.eclipse.ext.osaasjdbc;

import java.beans.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.management.RuntimeErrorException;

import org.jkiss.dbeaver.model.app.DBPPlatform;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCConnectionConfigurer;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.runtime.DBWorkbench;
public class OSaaSJDBCConnectionConfigurerAdapter implements JDBCConnectionConfigurer {

	@Override
	public void beforeConnection(DBRProgressMonitor monitor, DBPConnectionConfiguration connectionInfo,
			Properties connectProps) throws DBCException {
		// TODO Auto-generated method stub
		DBPPlatform dbpp = DBWorkbench.getPlatform();
		Properties p = connectProps;
		connectProps.setProperty("DbeaverProjectName", dbpp.getApplication().getDefaultProjectName());
		connectProps.setProperty("Dbeaver", "true");
		
	}

	@Override
	public void afterConnection(DBRProgressMonitor monitor, DBPConnectionConfiguration connectionInfo,
			Properties connectProps, Connection connection, Throwable error)   {
		// TODO Auto-generated method stub
		try {
			String metaDbFile =  connection.getClientInfo("metadataDbFile");
			File f = new File(metaDbFile);
			if(!f.exists()) {
				String fname = f.getName();
				String mess = String.format( 
"""
 It looks like the metadata file is missing.
 Please open https://osaasjdbc.gnimsys.com/driver/%s.zip
 and extract the %s to %s
""",fname,fname,metaDbFile);
				throw new RuntimeException(mess);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
