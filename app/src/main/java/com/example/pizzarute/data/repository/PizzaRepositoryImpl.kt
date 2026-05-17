package com.example.pizzarute.data.repository

import com.example.pizzarute.data.remote.AmplifyPizzaRemoteDataSource
import com.example.pizzarute.domain.model.Order
import com.example.pizzarute.domain.model.Pizza
import com.example.pizzarute.domain.repository.PizzaRepository

class PizzaRepositoryImpl(
    private val dataSource: AmplifyPizzaRemoteDataSource
) : PizzaRepository {

    override suspend fun fetchCatalog(): List<Pizza> {
        return dataSource.fetchCatalog()
    }

    override suspend fun createOrder(pizza: Pizza, quantity: Int): Order {
        return dataSource.createOrder(pizza, quantity)
    }

    override suspend fun uploadReceipt(fileName: String): String {
        return dataSource.uploadReceipt(fileName)
    }
}
