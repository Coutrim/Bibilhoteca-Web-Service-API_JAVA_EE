package com.coutrim.bibilhoteca_rest_service.service;

import com.coutrim.bibilhoteca_rest_service.dao.LivrosDAO;
import com.coutrim.bibilhoteca_rest_service.dto.LivrosDTO;
import com.coutrim.bibilhoteca_rest_service.exception.BibliotecaException;
import com.coutrim.bibilhoteca_rest_service.model.Livros;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.ws.rs.core.Response;
import java.util.List;


@Stateless
public class LivrosService {

    @Inject
    LivrosDAO livrosDAO;

    public Livros salvarLivro(LivrosDTO livroDTO) {
        try {
            if (livroDTO == null || livroDTO.getTitulo() == null || livroDTO.getAutor() == null
                    || livroDTO.getTitulo() == "" || livroDTO.getAutor() == "" || livroDTO.getDisponivel() == null ) {
                throw new IllegalArgumentException("Dados inválidos para salvamento ou campos obrigatórios faltando.");
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

    public List<Livros> listarLivros() throws Exception {
        try {
           return livrosDAO.listarLivros();
        } catch (Exception e){
            throw new Exception("Não foi possível listar os livros", e);
        }

    }

    public Livros buscarLivroPorId(Long livroId) throws Exception {
        try {
            return livrosDAO.buscarLivroPorId(livroId);
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro ao buscar o livro", e);
        }
    }

    public void  deletarLivro(Long id) throws Exception {
        try {
             livrosDAO.removerLivro(id);
        }catch (Exception e){
            throw new Exception("Ocorreu um erro ao remover o livro", e);
        }
    }



    public Livros editarLivro(Long livroId, LivrosDTO livroDTO) throws Exception {
        try {

            if (livroDTO == null || livroDTO.getTitulo() == null || livroDTO.getAutor() == null
                    || livroDTO.getTitulo() == "" || livroDTO.getAutor() == "" || livroDTO.getDisponivel() == null ) {
                throw new IllegalArgumentException("Dados inválidos para salvamento ou campos obrigatórios faltando.");
            }

            Livros livroExistente = livrosDAO.buscarLivroPorId(livroId);
            if (livroExistente == null) {
                throw new Exception("Livro não encontrado com o ID fornecido");
            }

            // Atualiza os campos do livroExistente com os valores do livroDTO
            livroExistente.setTitulo(livroDTO.getTitulo());
            livroExistente.setAutor(livroDTO.getAutor());
            livroExistente.setDisponivel(livroDTO.getDisponivel());

            return livrosDAO.atualizarLivro(livroExistente); // Atualiza no banco de dados

        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
}

