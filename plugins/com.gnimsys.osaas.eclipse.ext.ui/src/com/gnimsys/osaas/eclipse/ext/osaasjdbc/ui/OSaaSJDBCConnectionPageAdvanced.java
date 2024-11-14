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
package com.gnimsys.osaas.eclipse.ext.osaasjdbc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.preferences.DBPPreferenceStore;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.dialogs.connection.ConnectionPageAbstract;
import org.jkiss.utils.CommonUtils;


import com.gnimsys.osaas.eclipse.ext.osaasjdbc.ui.internal.OSaaSJDBCMessages;

public class OSaaSJDBCConnectionPageAdvanced extends ConnectionPageAbstract {

    private Combo sqlDollarQuoteBehaviorCombo;
    private Text licenseKey;
    private Text licenseMIdent;
    public OSaaSJDBCConnectionPageAdvanced() {
        setTitle("OSaaSJDBC");
    }

    @Override
    public void createControl(Composite parent) {
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayout(new GridLayout(1, false));
        group.setLayoutData(new GridData(GridData.FILL_BOTH));

        {
            Group sqlGroup = new Group(group, SWT.NONE);
            sqlGroup.setText(OSaaSJDBCMessages.dialog_setting_sql);
            sqlGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
            sqlGroup.setLayout(new GridLayout(2, false));

            
            
            
            sqlDollarQuoteBehaviorCombo = UIUtils.createLabelCombo(sqlGroup, OSaaSJDBCMessages.dialog_setting_sql_dd_label, SWT.DROP_DOWN | SWT.READ_ONLY);
            sqlDollarQuoteBehaviorCombo.add(OSaaSJDBCMessages.dialog_setting_sql_dd_string);
            sqlDollarQuoteBehaviorCombo.add(OSaaSJDBCMessages.dialog_setting_sql_dd_code_block);
        }
        {
        	Group licenseGroup = UIUtils.createControlGroup(group, OSaaSJDBCMessages.dialog_setting_licenseDetailes, 4, 0, 0);
        	licenseGroup.setText(OSaaSJDBCMessages.dialog_setting_licenseDetailes );
        	licenseGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        	//licenseGroup.setLayout(new GridLayout(2, false));
        	licenseKey = UIUtils.createLabelText(licenseGroup, OSaaSJDBCMessages.dialog_setting_licenseKey,null);
        	GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;            
            licenseKey.setLayoutData(gd);
            UIUtils.createControlLabel(licenseGroup, OSaaSJDBCMessages.label_database);

            licenseMIdent = new Text(licenseGroup, SWT.BORDER | SWT.DROP_DOWN);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 3;
            licenseMIdent.setLayoutData(gd);
        }
        setControl(group);
        loadSettings();
    }

    @Override
    public void loadSettings() {
        final DBPConnectionConfiguration config = getSite().getActiveDataSource().getConnectionConfiguration();
        final DBPPreferenceStore store = getSite().getActiveDataSource().getPreferenceStore();
        
    }

    @Override
    public void saveSettings(DBPDataSourceContainer dataSource) {
        
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
