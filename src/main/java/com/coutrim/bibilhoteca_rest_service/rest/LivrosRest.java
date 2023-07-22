package com.coutrim.bibilhoteca_rest_service.rest;

import com.coutrim.bibilhoteca_rest_service.dto.LivrosDTO;
import com.coutrim.bibilhoteca_rest_service.exception.BibliotecaException;
import com.coutrim.bibilhoteca_rest_service.model.Livros;
import com.coutrim.bibilhoteca_rest_service.service.LivrosService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

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
            return Response.created(URI.create(String.valueOf(
                    livrosService.salvarLivro(livroDTO)))).build();
        }catch (Exception e){
            if(e.getCause() instanceof BibliotecaException){
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            }
            return Response.serverError().build();
        }
    }

    @GET
    public  Response listarLivros(){
        try {
            List<Livros> livros = livrosService.listarLivros();
            return Response.status(Response.Status.OK).entity(livros).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar os livros: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarLivroPorId(@PathParam("id") Long id) {
        try {
            Livros livro = livrosService.buscarLivroPorId(id);
            if (livro == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Livro não encontrado")
                        .build();
            }
            return Response.status(Response.Status.OK).entity(livro).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar o livro: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarLivro(@PathParam("id") Long id, LivrosDTO livrosDTO) throws Exception {
        try {
            livrosService.editarLivro(id, livrosDTO);
            return Response.status(Response.Status.OK).entity(livrosDTO).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Rest: Erro ao editar o livro: " + e.getMessage())
                    .build();
        }
    }


    @DELETE
    @Path("/{id}")
    public Response deletarLivro(@PathParam("id") Long id) throws Exception {
        try {
            livrosService.deletarLivro(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir livro: " + e.getMessage())
                    .build();
        }
    }
}
