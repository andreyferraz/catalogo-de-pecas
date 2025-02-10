package com.andreyferraz.catalogo_de_pecas.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageWriter;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias() {
        return categoriaRepository.findAll();
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
            @RequestParam("categoria.id") UUID categoriaId,
            Model model) {
    
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/formulario";
        }
    
        if (imagem.isEmpty() && (produto.getId() == null || produto.getImagemPath() == null)) {
            model.addAttribute("errorMessage", "É obrigatório adicionar uma imagem ao produto.");
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/formulario";
        }
    
        if (!imagem.isEmpty()) {
            if (imagem.getSize() > 25 * 1024 * 1024) { // Máximo 25MB antes da compressão
                model.addAttribute("errorMessage", "O tamanho da imagem não pode exceder 25MB.");
                model.addAttribute("categorias", categoriaRepository.findAll());
                return "produtos/formulario";
            }
    
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
    
                String uniqueFilename = UUID.randomUUID().toString() + ".jpg";
                Path caminho = uploadPath.resolve(uniqueFilename);
    
                // Lendo a imagem original
                BufferedImage imagemOriginal = ImageIO.read(imagem.getInputStream());
    
                // Redimensionar a imagem para um máximo de 800x600 pixels
                BufferedImage imagemRedimensionada = redimensionarImagem(imagemOriginal, 800, 600);
    
                // Compressão JPEG para reduzir o tamanho final
                salvarJPEGComQualidade(imagemRedimensionada, caminho.toFile(), 0.6f); // Qualidade 60%
    
                // Excluir imagem antiga caso exista (edição)
                if (produto.getId() != null) {
                    Produto produtoExistente = produtoRepository.findById(produto.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Produto inválido"));
                    if (produtoExistente.getImagemPath() != null) {
                        Path imagemAntiga = Paths.get(uploadDir, 
                                Paths.get(produtoExistente.getImagemPath()).getFileName().toString());
                        Files.deleteIfExists(imagemAntiga);
                    }
                }
    
                produto.setImagemPath("/uploads/" + uniqueFilename);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Falha ao salvar a imagem.");
                model.addAttribute("categorias", categoriaRepository.findAll());
                return "produtos/formulario";
            }
        }
    
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));
        produto.setCategoria(categoria);
        produtoRepository.save(produto);
    
        return "redirect:/produtos";
    }
    
    // Método para redimensionar imagem
    private BufferedImage redimensionarImagem(BufferedImage imagemOriginal, int larguraMax, int alturaMax) {
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();
    
        // Se a imagem já for menor, não faz nada
        if (largura <= larguraMax && altura <= alturaMax) {
            return imagemOriginal;
        }
    
        // Calcula nova largura e altura mantendo a proporção
        float proporcao = Math.min((float) larguraMax / largura, (float) alturaMax / altura);
        int novaLargura = Math.round(largura * proporcao);
        int novaAltura = Math.round(altura * proporcao);
    
        BufferedImage novaImagem = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = novaImagem.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(imagemOriginal, 0, 0, novaLargura, novaAltura, null);
        g.dispose();
    
        return novaImagem;
    }
    
    // Método para salvar JPEG com compressão
    private void salvarJPEGComQualidade(BufferedImage imagem, File arquivo, float qualidade) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("Nenhum writer encontrado para JPEG!");
        }
        ImageWriter writer = writers.next();
    
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(arquivo)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(qualidade);
            writer.write(null, new javax.imageio.IIOImage(imagem, null, null), param);
        } finally {
            writer.dispose();
        }
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