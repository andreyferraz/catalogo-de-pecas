package com.andreyferraz.catalogo_de_pecas.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.andreyferraz.catalogo_de_pecas.models.CarrinhoItem;
import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final CategoriaRepository categoriaRepository;

    public LoginController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias() {
        return categoriaRepository.findAll();
    }

    @ModelAttribute("quantidadeCarrinho")
    public int getQuantidadeCarrinho(HttpSession session) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if (carrinho == null)
            return 0;

        return carrinho.stream().mapToInt(CarrinhoItem::getQuantidade).sum();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
