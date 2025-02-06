package com.andreyferraz.catalogo_de_pecas.controllers;

import com.andreyferraz.catalogo_de_pecas.models.CarrinhoItem;
import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import jakarta.servlet.http.HttpSession;

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

    @ModelAttribute("quantidadeCarrinho")
    public int getQuantidadeCarrinho(HttpSession session) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if (carrinho == null)
            return 0;

        return carrinho.stream().mapToInt(CarrinhoItem::getQuantidade).sum();
    }

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public String listarProdutosPorCategoria(@PathVariable UUID id, Model model) {
        model.addAttribute("categoria", categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida")));
        model.addAttribute("produtos", produtoRepository.findByCategoriaId(id));
        return "categorias/produtos";
    }

}
