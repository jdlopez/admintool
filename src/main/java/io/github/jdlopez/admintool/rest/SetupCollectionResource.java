package io.github.jdlopez.admintool.rest;

import io.github.jdlopez.admintool.domain.AdminSource;
import io.github.jdlopez.admintool.domain.database.DbTable;
import io.github.jdlopez.admintool.service.ConfigurationService;
import io.github.jdlopez.admintool.service.DatabaseDefinitionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
@Path("/api/setup")
public class SetupCollectionResource {

    @Inject
    ConfigurationService config;
    @Inject
    DatabaseDefinitionService dbService;

    @GET
    @Path("/source/{id}/tables")
    public List<DbTable> getTables(@PathParam("id") String id) throws SQLException {
        AdminSource source = config.getConfigInstances().getSources().stream()
                .filter(x -> id.equals(x.getName()))
                .findFirst()
                .orElseThrow(); // NotFoundException
        // source != null by force
        try (Connection conn = config.getDataSource(source).getConnection()) {
            return dbService.getTables(conn);
        }
    }

    @GET
    @Path("/source/{id}/table/{name}")
    public DbTable getTable(@PathParam("id") String sourceId, @PathParam("name") String tableName) throws SQLException {
        AdminSource source = config.getConfigInstances().getSources().stream()
                .filter(x -> sourceId.equals(x.getName()))
                .findFirst()
                .orElseThrow(); // NotFoundException
        // source != null by force
        try (Connection conn = config.getDataSource(source).getConnection()) {
            return dbService.getTableWithColumns(conn, tableName);
        }
    }



}
