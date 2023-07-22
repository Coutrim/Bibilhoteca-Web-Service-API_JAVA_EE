package com.coutrim.bibilhoteca_rest_service.dao;

import com.coutrim.bibilhoteca_rest_service.model.Livros;
import net.bytebuddy.implementation.bytecode.Throw;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Stateless
public class LivrosDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Livros> listarLivros(){
        Query query = entityManager.createQuery("select l from Livros l");
        return query.getResultList();
    }

    public Livros buscarLivroPorId(Long id){
        return entityManager.find(Livros.class, id);
    }

    public Livros inserirLivro(Livros livros){
        entityManager.persist(livros);
        return livros;
    }


    public Livros atualizarLivro(Livros livro) {
        return entityManager.merge(livro);
    }

    public void removerLivro(Long id) throws Exception {
        Livros livro = entityManager.find(Livros.class, id);
        if (livro != null) {
            entityManager.remove(livro);
        } else {
           throw new Exception("Livro não encontrado");
        }
    }
}
