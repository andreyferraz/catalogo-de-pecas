package com.andreyferraz.catalogo_de_pecas.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItem {
    private Produto produto;
    private int quantidade;
}
