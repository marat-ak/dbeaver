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
import org.jkiss.dbeaver.ext.generic.model.GenericSQLDialect;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCDatabaseMetaData;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCSession;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSource;
import org.jkiss.dbeaver.model.sql.SQLConstants;
import org.jkiss.dbeaver.model.sql.SQLUtils;
import org.jkiss.dbeaver.model.text.parser.TPRule;
import org.jkiss.dbeaver.model.text.parser.TPRuleProvider;
import java.util.Arrays;

public class OSaaSJDBCSQLDialect extends GenericSQLDialect implements TPRuleProvider {

    private static final String[][] OSaaSJDBC_BEGIN_END_BLOCK = new String[][]{
        {SQLConstants.BLOCK_BEGIN, SQLConstants.BLOCK_END},
        {"IF", SQLConstants.BLOCK_END}
    };

    // Referenced & Categorized from https://docs.osaasjdbc.com/en/sql-reference/functions-all 
    private static final String[] OSaaSJDBC_NUMERIC_FUNCTIONS = new String[] {
        "ABS",
        "ACOS",
        "ACOSH",
        "ASIN",
        "ASINH",
        "ATAN",
        "ATAN2",
        "ATANH",
        "CBRT",
        "CEIL",
        "COS",
        "COSH",
        "COT",
        "DEGREES",
        "EXP",
        "FACTORIAL",
        "FLOOR",
        "LN",
        "LOG",
        "MOD",
        "PI",
        "RADIANS",
        "ROUND",
        "SIGN",
        "SIN",
        "SINH",
        "SQRT",
        "TAN",
        "TANH",
        "WIDTH_BUCKET"
    };

    private static final String[] OSaaSJDBC_DATE_AND_TIME_FUNCTIONS = new String[] {
        "ADD_MONTHS",
        "DATE_PART",
        "DATE_TRUNC",
        "DATEADD",
        "DATEDIFF",
        "DAYNAME",
        "EXTRACT",
        "LAST_DAY",
        "MONTHNAME",
        "MONTHS_BETWEEN",
        "NEXT_DAY",
        "TIMEDIFF",
        "TIMESTAMPADD",
        "TIMESTAMPDIFF",
        "TRUNC"
    };

    private static final String[] OSaaSJDBC_CONTEXT_FUNCTIONS = new String[] {
        "CURRENT_DATABASE",
        "CURRENT_DATE",
        "CURRENT_SCHEMA",
        "CURRENT_TIME",
        "CURRENT_TIMESTAMP",
        "CURRENT_USER",
        "CURRENT_VERSION",
        "SYSDATE"
    };

    private static final String[] OSaaSJDBC_AGGREGATE_AND_WINDOW_FUNCTIONS = new String[] {
        "ANY_VALUE",
        "APPROX_COUNT_DISTINCT",
        "APPROX_PERCENTILE",
        "APPROX_TOP_K",
        "AVG",
        "CORR",
        "COUNT",
        "COUNT_IF",
        "COVAR_POP",
        "COVAR_SAMP",
        "KURTOSIS",
        "LISTAGG",
        "MAX",
        "MEDIAN",
        "MIN",
        "MODE",
        "PERCENTILE_CONT",
        "PERCENTILE_DISC",
        "REGR_AVGX",
        "REGR_AVGY",
        "REGR_COUNT",
        "REGR_INTERCEPT",
        "REGR_R2",
        "REGR_SLOPE",
        "REGR_SXX",
        "REGR_SXY",
        "REGR_SYY",
        "STDDEV",
        "STDDEV_POP",
        "STDDEV_SAMP",
        "SUM",
        "VAR_POP",
        "VAR_SAMP",
        "ARRAY_AGG",
        "GROUPING",
        "GROUPING_ID",
        "MAX_BY",
        "MIN_BY",
        "CUME_DIST",
        "DENSE_RANK",
        "FIRST_VALUE",
        "LAG",
        "LAST_VALUE",
        "LEAD",
        "NTH_VALUE",
        "NTILE",
        "PERCENT_RANK",
        "RANK",
        "RATIO_TO_REPORT",
        "ROW_NUMBER"
    };

    private static final String[] OSaaSJDBC_STRUCTURED_DATA_FUNCTIONS = new String[] {
        "ARRAY_CONTAINS",
        "ARRAY_DISTINCT",
        "ARRAY_EXCEPT",
        "ARRAY_MAX",
        "ARRAY_MIN",
        "ARRAY_POSITION",
        "ARRAY_REMOVE",
        "ARRAY_SIZE",
        "ARRAY_SORT",
        "ARRAYS_OVERLAP",
        "GET",
        "IS_BOOLEAN",
        "MAP_CONTAINS_KEY",
        "MAP_KEYS",
        "TYPEOF"
    };
    
    private static final String[] OSaaSJDBC_STRING_AND_BINARY_FUNCTIONS = new String[] {
        "ASCII",
        "BASE64_ENCODE",
        "BIT_LENGTH",
        "CHARINDEX",
        "COLLATION",
        "COMPRESS",
        "CONCAT_WS",
        "CONTAINS",
        "ENDSWITH",
        "HEX_ENCODE",
        "INITCAP",
        "INSERT",
        "LEFT",
        "LOWER",
        "LPAD",
        "LTRIM",
        "OCTET_LENGTH",
        "PARSE_URL",
        "POSITION",
        "REPEAT",
        "REPLACE",
        "REVERSE",
        "RIGHT",
        "RPAD",
        "RTRIM",
        "SOUNDEX",
        "SPACE",
        "SPLIT",
        "SPLIT_PART",
        "STARTSWITH",
        "TRANSLATE",
        "TRIM",
        "UNICODE",
        "UPPER",
        "REGEXP",
        "REGEXP_COUNT",
        "REGEXP_INSTR",
        "REGEXP_LIKE",
        "REGEXP_REPLACE",
        "REGEXP_SUBSTR",
        "RLIKE"
    };
   
    private static final String[] OSaaSJDBC_CONDITIONAL_FUNCTIONS = new String[] {
        "COALESCE",
        "DECODE",
        "EQUAL_NULL",
        "GREATEST",
        "IFF",
        "IFNULL",
        "LEAST",
        "NULLIF",
        "NULLIFZERO",
        "NVL",
        "NVL2",
        "ZEROIFNULL"
    };
    
    private static final String[] OSaaSJDBC_BITWISE_FUNCTIONS = new String[] {
        "BITAND",
        "BITNOT",
        "BITOR",
        "BITXOR",
        "GETBIT"
    };

    private static final String[] OSaaSJDBC_CONVERSION_FUNCTIONS = new String[] {
        "TO_BINARY",
        "TO_BOOLEAN",
        "TO_DOUBLE",
        "TRY_CAST",
        "TRY_TO_BINARY"
    };

    private static final String[] OSaaSJDBC_GEOSPATIAL_FUNCTIONS = new String[] {
        "ST_BUFFER",
        "ST_CENTROID",
        "ST_CONTAINS",
        "ST_DIFFERENCE",
        "ST_DIMENSION",
        "ST_DISJOINT",
        "ST_DISTANCE",
        "ST_ENDPOINT",
        "ST_ENVELOPE",
        "ST_LENGTH",
        "ST_POINTN",
        "ST_SETSRID",
        "ST_STARTPOINT",
        "ST_SYMDIFFERENCE",
        "ST_TRANSFORM",
        "ST_UNION",
        "ST_WITHIN",
        "ST_X",
        "ST_Y"
    };

    private static final String[] OSaaSJDBC_OTHER_FUNCTIONS = new String[] {
        "ENCRYPT",
        "FLATTEN",
        "HASH",
        "RANDOM",
        "TO_JSON"
    };

    public OSaaSJDBCSQLDialect() {
        super("OSaaSJDBC", "osaasjdbc");
    }

    public void initDriverSettings(JDBCSession session, JDBCDataSource dataSource, JDBCDatabaseMetaData metaData) {
        super.initDriverSettings(session, dataSource, metaData);

        addSQLKeywords(
            Arrays.asList(
                "QUALIFY",
                "ILIKE",
                "PACKAGE",
                "PIPE",
                "STAGE",
                "STREAM",
                "TAG",
                "TASK"
            ));
        removeSQLKeyword("VIEWS");
        
        addFunctions(Arrays.asList(OSaaSJDBC_AGGREGATE_AND_WINDOW_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_BITWISE_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_CONDITIONAL_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_CONTEXT_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_CONVERSION_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_NUMERIC_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_STRING_AND_BINARY_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_STRUCTURED_DATA_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_GEOSPATIAL_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_DATE_AND_TIME_FUNCTIONS));
        addFunctions(Arrays.asList(OSaaSJDBC_OTHER_FUNCTIONS));
    }

    

    @Override
    public String[][] getBlockBoundStrings() {
        return OSaaSJDBC_BEGIN_END_BLOCK;
    }

    @NotNull
    @Override
    public String getSearchStringEscape() {
        // Without escaping of wildcards OSaaSJDBC reads all metadata directly from database and ignores specified objects names
        // #9875
        return "\\";
    }

    @NotNull
    @Override
    public MultiValueInsertMode getDefaultMultiValueInsertMode() {
        return MultiValueInsertMode.GROUP_ROWS;
    }

    
    @Override
    public boolean validIdentifierStart(char c) {
        return SQLUtils.isLatinLetter(c) || c == '_';
    }
    
    @Override
    public boolean validIdentifierPart(char c, boolean quoted) {
        return SQLUtils.isLatinLetter(c) || Character.isDigit(c) || c == '_' || (quoted && validCharacters.indexOf(c) != -1) || c == '$';
    }

    
    @Override
    public boolean mustBeQuoted(@NotNull String str, boolean forceCaseSensitive) {
        // Unquoted object identifiers:
        // * Start with a letter (A-Z, a-z) or an underscore (“_”).
        // * Contain only letters, underscores, decimal digits (0-9), and dollar signs (“$”).
        // * Are stored and resolved as uppercase characters (e.g. id is stored and resolved as ID).
        // https://docs.osaasjdbc.com/en/sql-reference/identifiers-syntax

        if (str.isBlank()) {
            return true;
        }
        return super.mustBeQuoted(str, forceCaseSensitive);
    }
    
    @Override
    public String[] getSingleLineComments() {
        return new String[]{SQLConstants.SL_COMMENT, "//"};
    }

	@Override
	public TPRule[] extendRules(DBPDataSourceContainer dataSource, RulePosition position) {
		// TODO Auto-generated method stub
		return null;
	}
}
