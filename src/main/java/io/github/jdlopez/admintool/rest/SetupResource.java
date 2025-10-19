package io.github.jdlopez.admintool.rest;

import io.github.jdlopez.admintool.domain.AdminSource;
import io.github.jdlopez.admintool.domain.DbDriverType;
import io.github.jdlopez.admintool.service.ConfigurationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.io.IOException;
import java.util.Collection;

/**
 *
 */
@Path("/api/setup")
public class SetupResource {

    @Inject
    ConfigurationService config;

    @GET
    @Path("/source-types")
    public Collection<DbDriverType> sourceTypes() {
        return config.getDrivers();
    }

    @GET
    @Path("/source-type/{id}")
    public AdminSource sourceFromType(@PathParam("id") String id) {
        DbDriverType d = config.getDriver(id);
        AdminSource source = new AdminSource();
        source.setDriver(d.getDriverName());
        source.setUrl(d.getUrlMask());
        //source.setProperties(d.); se puden poner d.getPropertyNames() como keys
        return source;
    }

    @POST
    @Path("/save")
    public void save() throws IOException {
        config.save();
    }
}
