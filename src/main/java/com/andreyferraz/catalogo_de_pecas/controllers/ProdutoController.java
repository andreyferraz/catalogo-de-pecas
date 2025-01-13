package com.andreyferraz.catalogo_de_pecas.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andreyferraz.catalogo_de_pecas.models.Produto;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    
    public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return "produtos/listar";
    }

    @GetMapping("/novo")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "produtos/formulario";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid Produto produto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/formulario";
        }
        produtoRepository.save(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable UUID id, Model model) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido"));
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "produtos/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido"));
        produtoRepository.delete(produto);
        return "redirect:/produtos";
    }
}
