package org.example.repository;

import org.example.model.Produto;

import java.util.List;

public interface ProdutoRepository {
    Produto save(Produto produto);

    List<Produto> findAll();

    Produto findById();

    Produto update(Produto produto);

    void deleteById(int id);
}
