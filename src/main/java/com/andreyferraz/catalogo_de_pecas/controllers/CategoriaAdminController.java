package com.andreyferraz.catalogo_de_pecas.controllers;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categorias")
public class CategoriaAdminController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaAdminController(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias(){
        return categoriaRepository.findAll();
    }

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias/listar";
    }

    @GetMapping("/nova")
    public String exibirFomularioCadastro(Model model){
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @PostMapping("/salvar")
    public String salvarCategoria(@Valid @ModelAttribute Categoria categoria, BindingResult result){
        if(result.hasErrors()){
            return "categorias/formulario";
        }
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable UUID id, Model model){
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));
        model.addAttribute("categoria", categoria);
        return "categorias/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable UUID id){
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));
        categoriaRepository.delete(categoria);
        return "redirect:/categorias";
    }

}
