package com.andreyferraz.catalogo_de_pecas.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.andreyferraz.catalogo_de_pecas.models.CarrinhoItem;
import com.andreyferraz.catalogo_de_pecas.models.Produto;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public HomeController(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @ModelAttribute("quantidadeCarrinho")
    public int getQuantidadeCarrinho(HttpSession session) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if (carrinho == null)
            return 0;

        return carrinho.stream().mapToInt(CarrinhoItem::getQuantidade).sum();
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
