package com.andreyferraz.catalogo_de_pecas.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;

@Controller
public class LoginController {

    private final CategoriaRepository categoriaRepository;

    public LoginController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias(){
        return categoriaRepository.findAll();
    }
    

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
