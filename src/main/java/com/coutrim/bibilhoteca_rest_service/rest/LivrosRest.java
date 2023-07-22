package com.coutrim.bibilhoteca_rest_service.rest;

import com.coutrim.bibilhoteca_rest_service.dto.LivrosDTO;
import com.coutrim.bibilhoteca_rest_service.exception.BibliotecaException;
import com.coutrim.bibilhoteca_rest_service.model.Livros;
import com.coutrim.bibilhoteca_rest_service.service.LivrosService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JsonIgnoreProperties
public class LivrosRest {

    @Inject
    LivrosService livrosService;

    @POST
    public Response salvarLivro(LivrosDTO livroDTO){
        try{
            return Response.created(URI.create(String.valueOf(livrosService.salvarLivro(livroDTO).getId()))).build();
        }catch (Exception e){
            if(e.getCause() instanceof BibliotecaException){
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            }
            return Response.serverError().build();
        }
    }
}
