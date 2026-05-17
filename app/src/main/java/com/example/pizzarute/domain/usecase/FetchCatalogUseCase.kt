package com.example.pizzarute.domain.usecase

import com.example.pizzarute.domain.model.Pizza
import com.example.pizzarute.domain.repository.PizzaRepository

class FetchCatalogUseCase(
    private val repository: PizzaRepository
) {
    suspend operator fun invoke(): List<Pizza> {
        return repository.fetchCatalog()
    }
}
