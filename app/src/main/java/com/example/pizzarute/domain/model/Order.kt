package com.example.pizzarute.domain.model

data class Order(
    val id: String,
    val pizza: Pizza,
    val quantity: Int,
    val total: Double,
    val status: String
)
