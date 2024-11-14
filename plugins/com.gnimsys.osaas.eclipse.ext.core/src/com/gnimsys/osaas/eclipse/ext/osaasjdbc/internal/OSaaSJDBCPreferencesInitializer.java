
package com.gnimsys.osaas.eclipse.ext.osaasjdbc.internal;


import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.jkiss.dbeaver.ModelPreferences;
import org.jkiss.dbeaver.ext.oracle.model.OracleConstants;
import org.jkiss.dbeaver.model.impl.preferences.BundlePreferenceStore;
import org.jkiss.dbeaver.model.preferences.DBPPreferenceStore;
import org.jkiss.dbeaver.runtime.DBWorkbench;
import org.jkiss.dbeaver.ui.editors.sql.SQLPreferenceConstants;
import org.jkiss.dbeaver.utils.PrefUtils;



public class OSaaSJDBCPreferencesInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        // Init default preferences
        DBPPreferenceStore store = new BundlePreferenceStore(OSaaSJDBCActivator.getDefault().getBundle());
        //PrefUtils.setDefaultPreferenceValue(store, "script.sql.delimiter", ";;");
        DBPPreferenceStore storePlatform = DBWorkbench.getPlatform().getPreferenceStore();
        // Common
        //PrefUtils.setDefaultPreferenceValue(storePlatform, "script.sql.delimiter", ";;");
        PrefUtils.setDefaultPreferenceValue(storePlatform, SQLPreferenceConstants.EDITOR_CONNECT_ON_ACTIVATE, false);
        PrefUtils.setDefaultPreferenceValue(storePlatform, SQLPreferenceConstants.EDITOR_SEPARATE_CONNECTION, false);
        PrefUtils.setDefaultPreferenceValue(storePlatform, ModelPreferences.SCRIPT_STATEMENT_DELIMITER, ";;");
        PrefUtils.setDefaultPreferenceValue(storePlatform, ModelPreferences.SCRIPT_STATEMENT_DELIMITER_BLANK, false);
        
        
        
        

        // Common
        
    }
}
