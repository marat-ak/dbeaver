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


import com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.OSaaSJDBCConstants;
import com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.auth.OSaaSJDBCAuthModelOSaaSJDBC;
import com.gnimsys.osaas.eclipse.ext.osaasjdbc.ui.internal.OSaaSJDBCMessages;
import com.gnimsys.osaas.eclipse.ext.osaasjdbc.ui.internal.OSaaSJDBCUIActivator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.Log;

import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCResultSet;
import org.jkiss.dbeaver.model.exec.DBCSession;
import org.jkiss.dbeaver.model.exec.DBCStatement;
import org.jkiss.dbeaver.model.exec.DBCStatementType;
import org.jkiss.dbeaver.ui.IDataSourceConnectionTester;
import org.jkiss.dbeaver.ui.IDialogPageProvider;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.dialogs.connection.ConnectionPageWithAuth;
import org.jkiss.dbeaver.ui.dialogs.connection.DriverPropertiesDialogPage;
import org.jkiss.utils.CommonUtils;

/**
 * OSaaSJDBCConnectionPage
 */
public class OSaaSJDBCConnectionPage extends ConnectionPageWithAuth implements IDialogPageProvider, IDataSourceConnectionTester
{
    private static final Log log = Log.getLog(OSaaSJDBCConnectionPage.class);

    private Text hostText;
    private ComboViewer  sqlType;
    /*private Text portText;
    private Combo dbText;
    private Combo warehouseText;
    private Combo schemaText;
    */
    private static ImageDescriptor logoImage = OSaaSJDBCUIActivator.getImageDescriptor("icons/osaasjdbc_logo.png"); //$NON-NLS-1$

    @Override
    public void dispose()
    {
        super.dispose();
    }

    @Override
    public void createControl(Composite composite)
    {
        setImageDescriptor(logoImage);

        Composite control = new Composite(composite, SWT.NONE);
        control.setLayout(new GridLayout(1, false));
        control.setLayoutData(new GridData(GridData.FILL_BOTH));
        //ModifyListener textListener = e -> site.updateButtons();
        ControlsListener controlModifyListener = new ControlsListener();
        DBPConnectionConfiguration connectionInfo = site.getActiveDataSource().getConnectionConfiguration();
        {
            Composite addrGroup = UIUtils.createControlGroup(control, OSaaSJDBCMessages.label_connection, 1, 0, 0);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            addrGroup.setLayoutData(gd);

            UIUtils.createControlLabel(addrGroup, OSaaSJDBCMessages.label_host);

            hostText = new Text(addrGroup, SWT.BORDER);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;
            hostText.setLayoutData(gd);
            hostText.addModifyListener(controlModifyListener);
            /*UIUtils.createControlLabel(addrGroup, "SQL source");
            sqlType = new ComboViewer(addrGroup, SWT.BORDER |SWT.DROP_DOWN | SWT.READ_ONLY);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 3;
            
            //sqlType..setLayoutData(gd);
            sqlType.setContentProvider(ArrayContentProvider.getInstance());
            sqlType.setLabelProvider(new LabelProvider() {
                @Override
                public String getText(Object element) {
                    if (element instanceof ComboViewerElement) {
                    	ComboViewerElement el = (ComboViewerElement) element;
                        return el.getComboText();
                    }
                    return super.getText(element);
                }
            });
            sqlType.addSelectionChangedListener(controlModifyListener);           
            final ComboViewerElement[] sqlTypesList = new ComboViewerElement[] { new ComboViewerElement("Regular sql based on Tables/Views",OSaaSJDBCConstants.PROP_SQL_TYPE_REGULAR),
                    new ComboViewerElement("Logical sql based on Subject Area",OSaaSJDBCConstants.PROP_SQL_TYPE_SUBJECT) };
            sqlType.setInput(sqlTypesList);
            
            sqlType.addSelectionChangedListener(new ISelectionChangedListener() {
                @Override
                public void selectionChanged(SelectionChangedEvent event) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    ComboViewerElement el = (ComboViewerElement)selection.getFirstElement();
                    connectionInfo.setProperty("sqlType", el.value);
                    
                    //sqlType.refresh();
                    createAuthPanel(control, 1);
                    updateUI();
                    
                }
            });*/
 /*           UIUtils.createControlLabel(addrGroup, OSaaSJDBCMessages.label_port);

            portText = new Text(addrGroup, SWT.BORDER);
            gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
            gd.widthHint = UIUtils.getFontHeight(portText) * 7;
            portText.setLayoutData(gd);
            portText.addVerifyListener(UIUtils.getIntegerVerifyListener(Locale.getDefault()));
            portText.addModifyListener(textListener);

            UIUtils.createControlLabel(addrGroup, OSaaSJDBCMessages.label_database);

            dbText = new Combo(addrGroup, SWT.BORDER | SWT.DROP_DOWN);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 3;
            dbText.setLayoutData(gd);
            dbText.addModifyListener(textListener);

            UIUtils.createControlLabel(addrGroup, OSaaSJDBCMessages.label_warehouse);

            warehouseText = new Combo(addrGroup, SWT.BORDER | SWT.DROP_DOWN);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 3;
            warehouseText.setLayoutData(gd);
            warehouseText.addModifyListener(textListener);

            UIUtils.createControlLabel(addrGroup, OSaaSJDBCMessages.label_schema);

            schemaText = new Combo(addrGroup, SWT.BORDER);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 3;
            schemaText.setLayoutData(gd);
            schemaText.addModifyListener(textListener);
*/            
        }

      /* {
            Composite ph = UIUtils.createPlaceholder(control, 2);
            UIUtils.createInfoLabel(ph, ""); //$NON-NLS-1$
            Link testLink = new Link(ph, SWT.NONE);
            testLink.setText(OSaaSJDBCMessages.label_click_on_test_connection);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
            gd.grabExcessHorizontalSpace = true;
            testLink.setLayoutData(gd);
            testLink.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    site.testConnection();
                }
            });
        }*/
   
        createAuthPanel(control, 1);
        createDriverPanel(control);

        setControl(control);
    }

    @Override
    public boolean isComplete()
    {
        return hostText != null &&
            !CommonUtils.isEmpty(hostText.getText());
    }

    @Override
    public void loadSettings()
    {
        super.loadSettings();

        // Load values from new connection info
        DBPConnectionConfiguration connectionInfo = site.getActiveDataSource().getConnectionConfiguration();
        String hostName = connectionInfo.getHostName();
        if (hostText != null) {
            if (CommonUtils.isEmpty(hostName) || hostName.contains("localhost")) {
            	String connUrl = connectionInfo.getUrl();
            	if(!CommonUtils.isEmpty(connUrl) && connUrl.startsWith("jdbc:osaas:")) {
            		String targetUrl = connUrl.substring("jdbc:osaas:".length());
            		String[] urlData = targetUrl.split(":");
            		if(urlData.length>0) {
            			hostText.setText(urlData[0]);
            		}
                    
            	}
                
            } else {
                hostText.setText(connectionInfo.getHostName());
            }
        }
      /*  if (portText != null) {
            if (!CommonUtils.isEmpty(sqlType.getHostPort())) {
                portText.setText(String.valueOf(sqlType.getHostPort()));
            } else if (site.getDriver().getDefaultPort() != null) {
                portText.setText(site.getDriver().getDefaultPort());
            } else {
                portText.setText(""); //$NON-NLS-1$
            }
        }
        if (dbText != null) {
            String databaseName = sqlType.getDatabaseName();
            if (CommonUtils.isEmpty(databaseName)) {
                databaseName = OSaaSJDBCConstants.DEFAULT_DB_NAME;
            }
            dbText.setText(databaseName);
        }
        if (warehouseText != null) {
            warehouseText.setText(CommonUtils.notEmpty(sqlType.getServerName()));
        }
        if (schemaText != null) {
            schemaText.setText(CommonUtils.notEmpty(sqlType.getProviderProperty(OSaaSJDBCConstants.PROP_SCHEMA)));
        }*/
    }

    @NotNull
    @Override
    protected String getDefaultAuthModelId(DBPDataSourceContainer dataSource) {
        return OSaaSJDBCAuthModelOSaaSJDBC.ID;
    }

    @Override
    public void saveSettings(DBPDataSourceContainer dataSource)
    {
        DBPConnectionConfiguration connectionInfo = dataSource.getConnectionConfiguration();
        if (hostText != null) {
            connectionInfo.setHostName(hostText.getText().trim());
        }
        connectionInfo.setProperty(GROUP_CONNECTION_MODE, GROUP_CONNECTION);
        /*if (portText != null) {
            sqlType.setHostPort(portText.getText().trim());
        }
        if (dbText != null) {
            sqlType.setDatabaseName(dbText.getText().trim());
        }
        if (warehouseText != null) {
            sqlType.setServerName(warehouseText.getText().trim());
        }
        if (schemaText != null) {
            sqlType.setProviderProperty(OSaaSJDBCConstants.PROP_SCHEMA, schemaText.getText().trim());
        }*/
        super.saveSettings(dataSource);
    }

    @Override
    public void testConnection(DBCSession session) {
        try {
            /*loadDictList(session, dbText, "SHOW DATABASES"); //$NON-NLS-1$
            loadDictList(session, warehouseText, "SHOW WAREHOUSES"); //$NON-NLS-1$
            loadDictList(session, schemaText, "SHOW SCHEMAS"); //$NON-NLS-1$
            */
            //loadDictList(session, roleText, "SHOW ROLES"); //$NON-NLS-1$
        } catch (Exception e) {
            log.error(e);
        }
    }

    private static void loadDictList(DBCSession session, Combo combo, String query) throws DBCException {
        List<String> result = new ArrayList<>();
        session.getProgressMonitor().subTask("Exec " + query); //$NON-NLS-1$
        try (DBCStatement dbStat = session.prepareStatement(DBCStatementType.QUERY, query, false, false, false)) {
            dbStat.executeStatement();
            try (DBCResultSet dbResult = dbStat.openResultSet()) {
                while (dbResult.nextRow()) {
                    result.add(CommonUtils.toString(dbResult.getAttributeValue("name"))); //$NON-NLS-1$
                }
            }
        }
        UIUtils.asyncExec(() -> {
            String oldText = combo.getText();
            if (!result.contains(oldText)) {
                result.add(0, oldText);
            }
            if (!result.contains("")) { //$NON-NLS-1$
                result.add(0, ""); //$NON-NLS-1$
            }
            combo.setItems(result.toArray(new String[0]));
            combo.setText(oldText);
        });
    }
    private void updateUI()
    {
        
            site.updateButtons();
        
    }
    private class ControlsListener implements ModifyListener, SelectionListener ,ISelectionChangedListener{
        @Override
        public void modifyText(ModifyEvent e) {
            updateUI();
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            updateUI();
        }
        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            updateUI();
        }
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// TODO Auto-generated method stub
			//connectionInfo.setProperty(GROUP_CONNECTION_MODE, GROUP_CONNECTION);
			
		}
    }
    @Override
    public IDialogPage[] getDialogPages(boolean extrasOnly, boolean forceCreate)
    {
        return new IDialogPage[] {
            //new OSaaSJDBCConnectionPageAdvanced(),
            new DriverPropertiesDialogPage(this)
        };
    }

}
