package com.gnimsys.osaas.eclipse.ext.osaasjdbc;

import org.eclipse.core.runtime.IAdapterFactory;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCConnectionConfigurer;

import com.gnimsys.osaas.eclipse.ext.osaasjdbc.model.OSaaSJDBCDataSourceOracleType;


public class OSaaSJDBCSourceProviderAdapterFactory implements IAdapterFactory {

private static final Class<?>[] CLASSES = new Class[] { OSaaSJDBCConnectionConfigurerAdapter.class };
    
    @Override
    public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
        if (adaptableObject instanceof OSaaSJDBCDataSourceOracleType) {
            if (adapterType == JDBCConnectionConfigurer.class) {
                //return adapterType.cast(new ExasolDialectRules());
            	return adapterType.cast(new OSaaSJDBCConnectionConfigurerAdapter());
            }
        }
        return null;
    }

    @Override
    public Class<?>[] getAdapterList() {
        return CLASSES;
    }
}
