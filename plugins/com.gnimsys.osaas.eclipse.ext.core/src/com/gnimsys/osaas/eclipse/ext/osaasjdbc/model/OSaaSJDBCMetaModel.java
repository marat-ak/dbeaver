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
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBDatabaseException;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.generic.model.*;
import org.jkiss.dbeaver.ext.generic.model.meta.GenericMetaModel;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBPEvaluationContext;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.exec.DBCQueryTransformProvider;
import org.jkiss.dbeaver.model.exec.DBCQueryTransformType;
import org.jkiss.dbeaver.model.exec.DBCQueryTransformer;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCPreparedStatement;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCSession;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCStatement;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.impl.sql.QueryTransformerLimit;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.utils.CommonUtils;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

/**
 * OSaaSJDBCMetaModel
 */
public class OSaaSJDBCMetaModel extends GenericMetaModel
{
    public OSaaSJDBCMetaModel() {
        super();
    }

    @Override
    public GenericDataSource createDataSourceImpl(DBRProgressMonitor monitor, DBPDataSourceContainer container) throws DBException {
        return new OSaaSJDBCDataSourceGeneric(monitor, container, this);
    }

    

    @Override
    public String getTableDDL(@NotNull DBRProgressMonitor monitor, @NotNull GenericTableBase sourceObject, @NotNull Map<String, Object> options) throws DBException {
        GenericDataSource dataSource = sourceObject.getDataSource();
        boolean isView = sourceObject.isView();
        try (JDBCSession session = DBUtils.openMetaSession(monitor, sourceObject, "Read OSaaSJDBC object DDL")) {
            try (JDBCPreparedStatement dbStat = session.prepareStatement(
                    "SELECT GET_DDL('" + (isView ? "VIEW" : "TABLE") + "', '" +
                        sourceObject.getFullyQualifiedName(DBPEvaluationContext.DDL) + "', TRUE) "))
            {
                try (JDBCResultSet dbResult = dbStat.executeQuery()) {
                    StringBuilder sql = new StringBuilder();
                    while (dbResult.nextRow()) {
                        sql.append(dbResult.getString(1));
                    }
                    String result = sql.toString().trim();
                    while (result.endsWith(";")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            throw new DBDatabaseException(e, dataSource);
        }
    }

    @Override
    public boolean supportsTableDDLSplit(GenericTableBase sourceObject) {
        return false;
    }

    @Nullable
    @Override
    public Integer extractPrecisionOfNumericColumn(int valueType, long columnSize) {
        // Sometimes for some reason OSaaSJDBC returns NUMBER as BIGINT
        if (valueType == Types.NUMERIC || valueType == Types.DECIMAL || valueType == Types.BIGINT) {
            return Math.toIntExact(columnSize);
        }
        return null;
    }

    public String getViewDDL(@NotNull DBRProgressMonitor monitor, @NotNull GenericView sourceObject, @NotNull Map<String, Object> options) throws DBException {
        return getTableDDL(monitor, sourceObject, options);
    }

    @Override
    public String getProcedureDDL(DBRProgressMonitor monitor, GenericProcedure sourceObject) throws DBException {
        GenericDataSource dataSource = sourceObject.getDataSource();
        
        try (JDBCSession session = DBUtils.openMetaSession(monitor, sourceObject, "Read OSaaSJDBC object DDL")) {
            try (JDBCPreparedStatement dbStat = session.prepareStatement(
                "SELECT GET_DDL('"  + sourceObject.getProcedureType() + "', '" +
                    sourceObject.getProcedureSignature(monitor, false) + "', TRUE)"))
            {
                try (JDBCResultSet dbResult = dbStat.executeQuery()) {
                    StringBuilder sql = new StringBuilder();
                    while (dbResult.nextRow()) {
                        sql.append(dbResult.getString(1));
                    }
                    return sql.toString();
                }
            }
        } catch (SQLException e) {
            throw new DBDatabaseException(e, dataSource);
        }
    }

    @Override
    public boolean supportsOverloadedProcedureNames() {
        return false;
    }

    @Override
    public boolean isTableCommentEditable() {
        return false;
    }

    @Override
    public boolean isTableColumnCommentEditable() {
        return false;
    }

    


    private boolean supportsWildcards(@NotNull JDBCSession session, @NotNull GenericStructContainer owner) throws SQLException {
       
        return false;
    }

    @Nullable
    private String getSchemaNameForPattern(@NotNull JDBCSession session, boolean recognizeWildCards, @Nullable GenericSchema schema) {
        return schema == null || DBUtils.isVirtualObject(schema) ?
            null : recognizeWildCards ? JDBCUtils.escapeWildCards(session, schema.getName()) : schema.getName();
    }

    @NotNull
    private String getTableNameForPattern(
        @NotNull JDBCSession session,
        @NotNull GenericStructContainer owner,
        @Nullable GenericTableBase forParent,
        boolean recognizeWildCards)
    {
        return forParent == null ? owner.getDataSource().getAllObjectsPattern()
            : recognizeWildCards ? JDBCUtils.escapeWildCards(session, forParent.getName()) : forParent.getName();
    }
}
