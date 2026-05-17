package com.example.pizzarute.util

import com.example.pizzarute.domain.model.Pizza

object CatalogFallback {
    val pizzas = listOf(
        Pizza(
            id = "1",
            name = "Margherita",
            description = "Molho de tomate, muçarela, manjericão e azeite.",
            price = 34.90,
            ingredients = listOf("Molho", "Muçarela", "Manjericão")
        ),
        Pizza(
            id = "2",
            name = "Calabresa",
            description = "Calabresa fatiada, cebola e muçarela.",
            price = 39.90,
            ingredients = listOf("Calabresa", "Cebola", "Muçarela")
        ),
        Pizza(
            id = "3",
            name = "Frango com Catupiry",
            description = "Frango desfiado, catupiry e muçarela.",
            price = 42.90,
            ingredients = listOf("Frango", "Catupiry", "Muçarela")
        ),
        Pizza(
            id = "4",
            name = "Quatro Queijos",
            description = "Muçarela, parmesão, provolone e gorgonzola.",
            price = 44.90,
            ingredients = listOf("Muçarela", "Parmesão", "Provolone", "Gorgonzola")
        )
    )
}
