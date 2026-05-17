package com.example.pizzarute.domain.repository

import com.example.pizzarute.domain.model.Order
import com.example.pizzarute.domain.model.Pizza

interface PizzaRepository {
    suspend fun fetchCatalog(): List<Pizza>
    suspend fun createOrder(pizza: Pizza, quantity: Int): Order
    suspend fun uploadReceipt(fileName: String): String
}
