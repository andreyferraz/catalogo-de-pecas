package com.andreyferraz.catalogo_de_pecas.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.andreyferraz.catalogo_de_pecas.models.Produto;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

@Controller
public class HomeController {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public HomeController(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        // Adiciona os 16 últimos produtos cadastrados ao modelo
        List<Produto> ultimosProdutos = produtoRepository.findTop16ByOrderByIdDesc();
        model.addAttribute("ultimosProdutos", ultimosProdutos);
        return "index";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(@RequestParam String nome, Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        // Verifica se a lista de produtos está vazia
        if (produtos.isEmpty()) {
            // Adiciona mensagem ao modelo se nenhum produto for encontrado
            model.addAttribute("mensagem", "Não existe nenhum produto disponível com esse nome");
        } else {
            // Adiciona a lista de produtos ao modelo
            model.addAttribute("produtos", produtos);
        }

        // Flag indicando que uma pesquisa foi realizada
        model.addAttribute("pesquisaRealizada", true);

        return "index";
    }

}
