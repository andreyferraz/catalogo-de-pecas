package com.andreyferraz.catalogo_de_pecas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;

@Controller
public class HomeController {

    private final CategoriaRepository categoriaRepository;

    public HomeController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "index";
    }
}
