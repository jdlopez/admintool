package io.github.jdlopez.admintool.rest;

import io.github.jdlopez.admintool.domain.AdminSource;
import io.github.jdlopez.admintool.service.ConfigurationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

/**
 *
 */
@Path("/api/setup")
public class SetupSourceResource {

    @Inject
    ConfigurationService config;

    @GET()
    @Path("/sources")
    public List<AdminSource> sources() {
        return config.getConfigInstances().getSources();
    }

    @POST
    @Path("/sources")
    public AdminSource add(AdminSource adminSource) {
        config.getConfigInstances().getSources().add(adminSource);
        return adminSource;
    }

    @GET
    @Path("/source/{id}")
    public AdminSource sourceDetail(@PathParam("id") String id) {
        return config.getConfigInstances().getSources().stream()
                .filter(x -> id.equals(x.getName()))
                .findFirst()
                .orElse(null);
    }

    @DELETE
    @Path("/source/{id}")
    public boolean deleteDetail(@PathParam("id") String id) {
        List<AdminSource> lst = config.getConfigInstances().getSources();
        for (AdminSource x: lst) {
            if (id.equals(x.getName())) {
                return lst.remove(x);
            }
        }
        return false;
    }

    @POST
    @Path("/source/{id}")
    public AdminSource updateDetail(@PathParam("id") String id,
                                    AdminSource adminSource) {
        if ( deleteDetail(id) ) {
            config.getConfigInstances().getSources()
                    .add(adminSource);
            return adminSource;
        } else
            return null;
    }

}
