package me.escoffier.parasol;

import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;

public class Responses {


    public static Response notFound() {
        return Response.status(404).build();
    }

    public static Response noContentOrNotFound(Object object) {
        return object == null ? notFound() : Response.noContent().build();
    }

    public static Response created(int id)  {
        try {
            return Response.created(new URI("api/claims/" + id)).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
