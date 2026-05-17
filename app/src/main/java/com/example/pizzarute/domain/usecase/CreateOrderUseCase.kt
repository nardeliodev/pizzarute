package com.example.pizzarute.domain.usecase

import com.example.pizzarute.domain.model.Order
import com.example.pizzarute.domain.model.Pizza
import com.example.pizzarute.domain.repository.PizzaRepository

class CreateOrderUseCase(
    private val repository: PizzaRepository
) {
    suspend operator fun invoke(pizza: Pizza, quantity: Int): Order {
        return repository.createOrder(pizza, quantity)
    }
}
