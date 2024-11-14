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
package com.gnimsys.osaas.eclipse.ext.osaasjdbc.model;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.generic.model.GenericDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCExecutionContext;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//public class OSaaSJDBCDataSource extends GenericDataSource {
public class OSaaSJDBCDataSourceGeneric extends GenericDataSource {

    private static final String APPLICATION_PROPERTY = "application";

    public OSaaSJDBCDataSourceGeneric(DBRProgressMonitor monitor, DBPDataSourceContainer container, OSaaSJDBCMetaModel metaModel)
        throws DBException
    {
        super(monitor, container, metaModel, new OSaaSJDBCSQLDialect());
    }
    
    public OSaaSJDBCDataSourceGeneric(
        @NotNull DBRProgressMonitor monitor,
        @NotNull DBPDataSourceContainer container,
        @NotNull OSaaSJDBCMetaModel metaModel,
        @NotNull OSaaSJDBCSQLDialect dialect
    ) throws DBException {
        super(monitor, container, metaModel, dialect);
    }

    @Override
    protected Map<String, String> getInternalConnectionProperties(
        @NotNull DBRProgressMonitor monitor,
        @NotNull DBPDriver driver,
        @NotNull JDBCExecutionContext context,
        @NotNull String purpose,
        @NotNull DBPConnectionConfiguration connectionInfo
    ) {
        Map<String, String> props = new HashMap<>();

        // Backward compatibility - use legacy provider property
        // Newer versions use auth model
        

        return props;
    }

    @Override
    protected boolean isPopulateClientAppName() {
        return false;
    }

    @Override
    protected Properties getAllConnectionProperties(
        @NotNull DBRProgressMonitor monitor,
        JDBCExecutionContext context,
        String purpose,
        DBPConnectionConfiguration connectionInfo
    ) throws DBCException {
        Properties props = super.getAllConnectionProperties(monitor, context, purpose, connectionInfo);
        final String clientAppName = DBUtils.getClientApplicationName(container, context, null, false);
        final String appName = "DBeaver_" + clientAppName.replace(" ", "");
        props.put(APPLICATION_PROPERTY, appName);
        return props;
    }

    
}
