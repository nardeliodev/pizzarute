package com.example.pizzarute.domain.model

data class Pizza(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val ingredients: List<String>
)
