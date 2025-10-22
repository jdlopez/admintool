package io.github.jdlopez.admintool.service;

import io.github.jdlopez.admintool.domain.AdminSource;
import io.github.jdlopez.admintool.domain.database.DbColumn;
import io.github.jdlopez.admintool.domain.database.DbTable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DatabaseDefinitionService {

    @Inject
    ConfigurationService configurationService;

    public List<DbTable> getTables(Connection conn) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();

        ResultSet rs = md
                .getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});
        List<DbTable> tables = new ArrayList<>();
        while (rs.next()) {
            DbTable table = mapTable(rs);
            tables.add(table);
        }
        return tables;
    }

    protected DbTable mapTable(ResultSet rs) throws SQLException {
        DbTable table = new DbTable();
        table.setName( rs.getString("TABLE_NAME") );
        table.setSchema( rs.getString("TABLE_SCHEM") );
        table.setCatalog( rs.getString("TABLE_CAT") );
        table.setType( rs.getString("TABLE_TYPE") );
        return table;
    }

    protected DbColumn mapColumn(ResultSet rs) throws SQLException {
        DbColumn column = new DbColumn();
        column.setName( rs.getString("COLUMN_NAME") );
        column.setNullable( rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable );
        column.setDataType( rs.getInt("DATA_TYPE") );
        column.setTypeName( rs.getString("TYPE_NAME") );
        column.setSize( rs.getInt("COLUMN_SIZE") );
        column.setAutoincrement( "YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT")) );
        column.setRemarks( rs.getString("REMARKS") );
        return column;

    }

    public DbTable getTableWithColumns(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, tableName, new String[]{"TABLE"});
        if ( !rs.next() ) {
            rs.close();
            return null;
        }
        DbTable table = mapTable(rs);
        rs.close();
        table.setColumns(new ArrayList<>());
        rs = md.getColumns(null, null, tableName, null);
        while (rs.next()) {
            /*
TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
COLUMN_NAME String => column name
DATA_TYPE int => SQL type from java.sql.Types
TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
COLUMN_SIZE int => column size.
BUFFER_LENGTH is not used.
DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
NUM_PREC_RADIX int => Radix (typically either 10 or 2)
NULLABLE int => is NULL allowed.
columnNoNulls - might not allow NULL values
columnNullable - definitely allows NULL values
columnNullableUnknown - nullability unknown
REMARKS String => comment describing column (may be null)
COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
SQL_DATA_TYPE int => unused
SQL_DATETIME_SUB int => unused
CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
ORDINAL_POSITION int => index of column in table (starting at 1)
IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
YES --- if the column can include NULLs
NO --- if the column cannot include NULLs
empty string --- if the nullability for the column is unknown
SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
YES --- if the column is auto incremented
NO --- if the column is not auto incremented
empty string --- if it cannot be determined whether the column is auto incremented
IS_GENERATEDCOLUMN
             */
            DbColumn column = mapColumn(rs);
            table.getColumns().add(column);
        }
        rs.close();
        return table;
    }

}
