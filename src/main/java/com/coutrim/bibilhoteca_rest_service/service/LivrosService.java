package com.coutrim.bibilhoteca_rest_service.service;

import com.coutrim.bibilhoteca_rest_service.dao.LivrosDAO;
import com.coutrim.bibilhoteca_rest_service.dto.LivrosDTO;
import com.coutrim.bibilhoteca_rest_service.exception.BibliotecaException;
import com.coutrim.bibilhoteca_rest_service.model.Livros;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.ws.rs.core.Response;


@Stateless
public class LivrosService {

    @Inject
    LivrosDAO livrosDAO;

    public Livros salvarLivro(LivrosDTO livroDTO) {
        try {
            if (livroDTO == null || livroDTO.getTitulo() == null || livroDTO.getAutor() == null
                    || livroDTO.getTitulo() == "" || livroDTO.getAutor() == "" ) {
                throw new IllegalArgumentException("Dados inválidos para salvamento.");
            }

            Livros livro = new Livros();
            livro.setId(livroDTO.getId());
            livro.setDisponivel(livroDTO.getDisponivel());
            livro.setTitulo(livroDTO.getTitulo());
            livro.setAutor(livroDTO.getAutor());

            validarLivroParaSalvar(livro);
            livrosDAO.inserirLivro(livro);

            return livro;
        } catch (IllegalArgumentException e) {
            throw new BibliotecaException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }

    private void validarLivroParaSalvar(Livros livro) {
        if (livro.getTitulo() == null || livro.getAutor() == null) {
            throw new IllegalArgumentException("Dados inválidos para salvamento.");
        }
    }
}

