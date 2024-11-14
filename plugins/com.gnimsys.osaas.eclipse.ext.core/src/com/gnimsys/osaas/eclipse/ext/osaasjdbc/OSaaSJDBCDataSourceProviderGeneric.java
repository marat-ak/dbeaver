package com.gnimsys.osaas.eclipse.ext.osaasjdbc;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ext.generic.GenericDataSourceProvider;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.app.DBPPlatform;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSourceProvider;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;

import com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.OSaaSJDBCDataSourceGeneric;
import com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.OSaaSJDBCMetaModel;

public class OSaaSJDBCDataSourceProviderGeneric extends GenericDataSourceProvider {

	private static final Log log = Log.getLog(OSaaSJDBCDataSourceProviderGeneric.class);

    public OSaaSJDBCDataSourceProviderGeneric()
    {
    }

    @Override
    public void init(@NotNull DBPPlatform platform) {
    	System.out.print(this.getClass().toString()+" init");

    }

    @Override
    public long getFeatures()
    {
        return FEATURE_CATALOGS | FEATURE_SCHEMAS;
    }

    @Override
    public String getConnectionURL(DBPDriver driver, DBPConnectionConfiguration connectionInfo)
    {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:osaas:").append(connectionInfo.getHostName());
        
        return url.toString();
    }

    
/*
    @NotNull
    @Override
    public DBPDataSource openDataSource(
        @NotNull DBRProgressMonitor monitor,
        @NotNull DBPDataSourceContainer container)
        throws DBException
    {
        //return new OSaaSJDBCDataSourceGeneric(monitor, container, new OSaaSJDBCMetaModel());
    	return new OSaaSJDBCDataSourceGeneric(monitor, container, new OSaaSJDBCMetaModel());
    }
*/

}
