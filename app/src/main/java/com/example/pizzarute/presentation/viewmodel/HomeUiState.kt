package com.example.pizzarute.presentation.viewmodel

import com.example.pizzarute.domain.model.Order
import com.example.pizzarute.domain.model.Pizza

data class HomeUiState(
    val isLoading: Boolean = false,
    val apiMessage: String = "Aguardando sincronização com a API.",
    val receiptMessage: String = "Nenhum arquivo enviado ainda.",
    val catalog: List<Pizza> = emptyList(),
    val lastOrder: Order? = null,
    val uploadFileName: String = "comprovante.png"
)
