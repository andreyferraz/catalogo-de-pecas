package com.andreyferraz.catalogo_de_pecas.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.models.Produto;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    @Value("${upload.dir}")
    private String uploadDir;

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
    public String salvarProduto(
        @Valid Produto produto,
        BindingResult result,
        @RequestParam("imagem") MultipartFile imagem,
        @RequestParam("categoria") UUID categoriaId,
        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/formulario";
        }

        if (!imagem.isEmpty()) {
            try {
                // Caminho dentro do projeto, na pasta static/uploads
                Path uploadPath = Paths.get("C:/uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    System.out.println("Diretório de upload criado: " + uploadPath.toAbsolutePath().toString());
                }

                // Caminho completo do arquivo
                Path caminho = uploadPath.resolve(imagem.getOriginalFilename());
                System.out.println("Salvando imagem em: " + caminho.toAbsolutePath().toString());

                // Transferir o arquivo
                imagem.transferTo(caminho.toFile());
                System.out.println("Imagem salva com sucesso: " + caminho.toAbsolutePath().toString());

                // Configurar o caminho da imagem no produto
                produto.setImagemPath("/uploads/" + imagem.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Falha ao salvar a imagem. Por favor, tente novamente.");
                model.addAttribute("categorias", categoriaRepository.findAll());
                return "produtos/formulario";
            }
        } else {
            // Mantem a imagem existente se nenhuma nova imagem for enviada
            if (produto.getId() != null) {
                Produto produtoExistente = produtoRepository.findById(produto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto inválido"));
                produto.setImagemPath(produtoExistente.getImagemPath());
            }
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));
        produto.setCategoria(categoria);
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