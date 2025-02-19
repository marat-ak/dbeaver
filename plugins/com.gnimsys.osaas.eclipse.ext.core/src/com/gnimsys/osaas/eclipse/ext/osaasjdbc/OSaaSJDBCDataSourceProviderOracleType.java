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
package com.gnimsys.osaas.eclipse.ext.osaasjdbc;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.app.DBPPlatform;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSourceProvider;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;

import com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.OSaaSJDBCDataSourceOracleType;



public class OSaaSJDBCDataSourceProviderOracleType extends JDBCDataSourceProvider {

    private static final Log log = Log.getLog(OSaaSJDBCDataSourceProviderOracleType.class);

    public OSaaSJDBCDataSourceProviderOracleType()
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

    

    @NotNull
    @Override
    public DBPDataSource openDataSource(
        @NotNull DBRProgressMonitor monitor,
        @NotNull DBPDataSourceContainer container)
        throws DBException
    {
        //return new OSaaSJDBCDataSourceGeneric(monitor, container, new OSaaSJDBCMetaModel());
    	return new OSaaSJDBCDataSourceOracleType(monitor, container);
    }

}
