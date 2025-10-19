package io.github.jdlopez.admintool.service;

import io.github.jdlopez.admintool.domain.AdminSource;
import io.github.jdlopez.admintool.domain.database.DbTable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DatabaseDefinitionService {

    @Inject
    ConfigurationService configurationService;

    public List<DbTable> getTables(Connection conn) throws SQLException {
        ResultSet rs = conn.getMetaData()
                .getTables(null, null, null, new String[]{"TABLE"});
        List<DbTable> tables = new ArrayList<>();
        while (rs.next()) {
            DbTable table = new DbTable();
            table.setName( rs.getString("TABLE_NAME") );
            tables.add(table);
        }
        return tables;
    }
}
