package com.andreyferraz.catalogo_de_pecas.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andreyferraz.catalogo_de_pecas.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

}
