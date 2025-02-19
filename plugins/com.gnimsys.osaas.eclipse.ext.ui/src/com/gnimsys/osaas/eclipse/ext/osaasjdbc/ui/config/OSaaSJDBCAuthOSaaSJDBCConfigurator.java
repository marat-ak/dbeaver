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
package com.gnimsys.osaas.eclipse.ext.osaasjdbc.ui.config;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.access.DBAAuthModel;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.dialogs.connection.DatabaseNativeAuthModelConfigurator;

/**
 * OSaaSJDBC database native auth model config
 */
public class OSaaSJDBCAuthOSaaSJDBCConfigurator extends DatabaseNativeAuthModelConfigurator {
    @Override
    public void createControl(@NotNull Composite parent, DBAAuthModel<?> object, @NotNull Runnable propertyChangeListener) {
        ModifyListener textListener = e -> propertyChangeListener.run();

        usernameLabel = UIUtils.createLabel(parent, "Username:");
        usernameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        createUserNameControls(parent, propertyChangeListener);

        if (supportsPassword()) {
            createPasswordControls(parent, propertyChangeListener);
        }

        
    }

    

    @Override
    public void loadSettings(@NotNull DBPDataSourceContainer dataSource) {
        super.loadSettings(dataSource);

        DBPConnectionConfiguration connectionInfo = dataSource.getConnectionConfiguration();
        
    }

    @Override
    public void saveSettings(@NotNull DBPDataSourceContainer dataSource) {
        super.saveSettings(dataSource);

        
    }

    @Override
    public void resetSettings(@NotNull DBPDataSourceContainer dataSource) {
        super.resetSettings(dataSource);
    }

    protected boolean supportsPassword() {
        return true;
    }

}
