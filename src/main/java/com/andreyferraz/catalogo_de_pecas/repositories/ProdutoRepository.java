package com.andreyferraz.catalogo_de_pecas.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andreyferraz.catalogo_de_pecas.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    List<Produto> findByCategoriaId(UUID categoriaId);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findTop16ByOrderByIdDesc();
}
