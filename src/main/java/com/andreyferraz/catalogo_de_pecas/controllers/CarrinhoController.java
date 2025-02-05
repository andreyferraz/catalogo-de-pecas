package com.andreyferraz.catalogo_de_pecas.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.andreyferraz.catalogo_de_pecas.models.CarrinhoItem;
import com.andreyferraz.catalogo_de_pecas.models.Categoria;
import com.andreyferraz.catalogo_de_pecas.models.Produto;
import com.andreyferraz.catalogo_de_pecas.repositories.CategoriaRepository;
import com.andreyferraz.catalogo_de_pecas.repositories.ProdutoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;


    public CarrinhoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @ModelAttribute("categorias")
    public List<Categoria> carregarCategorias(){
        return categoriaRepository.findAll();
    }

    @GetMapping
    public String exibirCarrinho(HttpSession session, Model model) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if(carrinho == null){
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }
        model.addAttribute("carrinho", carrinho);
        return "carrinho/exibir";

    }

    @PostMapping("/adicionar")
    public String adicionarAoCarrinho(@RequestParam UUID produtoId, @RequestParam int quantidade, HttpSession session) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado!"));

        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }

        // Verifica se o produto já está no carrinho
        CarrinhoItem itemExistente = carrinho.stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
        } else {
            carrinho.add(new CarrinhoItem(produto, quantidade));
        }

        return "redirect:/carrinho";
    }

    @GetMapping("/remover/{produtoId}")
    public String removerDoCarrinho(@PathVariable UUID produtoId, HttpSession session) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if (carrinho != null) {
            carrinho.removeIf(item -> item.getProduto().getId().equals(produtoId));
        }
        return "redirect:/carrinho";
    }

    @PostMapping("/enviar")
    public String enviarPedido(@RequestParam String nome, @RequestParam String telefone, HttpSession session) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.isEmpty()) {
            return "redirect:/carrinho";
        }

        // Gera a mensagem para o WhatsApp
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Pedido de: ").append(nome).append("\n");
        mensagem.append("Telefone: ").append(telefone).append("\n");
        mensagem.append("Itens:\n");
        for (CarrinhoItem item : carrinho) {
            mensagem.append("- ").append(item.getProduto().getNome())
                    .append(" (Quantidade: ").append(item.getQuantidade()).append(")\n");
        }

        String mensagemFormatada = mensagem.toString().replace("\n", "%0A");
        //String urlWhatsApp = "https://api.whatsapp.com/send?phone=+5514998761984&text=" + mensagemFormatada;
        String urlWhatsApp = "https://api.whatsapp.com/send?phone=+5527981076157&text=" + mensagemFormatada;

        carrinho.clear();

        return "redirect:" + urlWhatsApp;
    }

}
