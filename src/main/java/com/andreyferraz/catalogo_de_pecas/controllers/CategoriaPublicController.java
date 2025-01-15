package com.andreyferraz.catalogo_de_pecas.controllers;

import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categorias")
public class CategoriaPublicController {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaPublicController(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias(){
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public String listarProdutosPorCategoria(@PathVariable UUID id, Model model){
        model.addAttribute("categoria", categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoria inválida")));
        model.addAttribute("produtos", produtoRepository.findByCategoriaId(id));
        return "categorias/produtos";
    }

}
