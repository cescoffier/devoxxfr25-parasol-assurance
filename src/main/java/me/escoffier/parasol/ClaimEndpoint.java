package me.escoffier.parasol;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static me.escoffier.parasol.Responses.created;
import static me.escoffier.parasol.Responses.noContentOrNotFound;

@Path("/api/claims")
public class ClaimEndpoint {

    private final ClaimService claimService;

    @Inject
    Logger log;

    public ClaimEndpoint(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GET
    public List<ClaimInfo> getClaims() {
        return claimService.getAllClaims();
    }


    @GET
    @Path("/{claimId}")
    public ClaimInfo getClaim(int claimId) {
        return claimService.get(claimId);
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response create(String txt) {
        var id = claimService.process(txt);
        return created(id);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(@RestForm File file) throws IOException {
        var content = Files.readString(file.toPath());
        log.info("Processing claim... ");
        try {
            var id = claimService.process(content);
            log.infof("Processed claim %d", id);
            return created(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }


    @DELETE
    @Path("/{claimId}")
    public Response delete(int claimId) {
        var deleted = claimService.delete(claimId);
        return noContentOrNotFound(deleted);
    }

}
