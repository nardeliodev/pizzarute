package com.example.pizzarute.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzarute.domain.model.Pizza
import com.example.pizzarute.domain.usecase.CreateOrderUseCase
import com.example.pizzarute.domain.usecase.FetchCatalogUseCase
import com.example.pizzarute.domain.usecase.UploadReceiptUseCase
import com.example.pizzarute.util.CatalogFallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fetchCatalogUseCase: FetchCatalogUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val uploadReceiptUseCase: UploadReceiptUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                apiMessage = "Carregando cardápio..."
            )

            runCatching { fetchCatalogUseCase() }
                .onSuccess { pizzas ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        catalog = if (pizzas.isNotEmpty()) pizzas else CatalogFallback.pizzas,
                        apiMessage = "Cardápio carregado via API."
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        catalog = CatalogFallback.pizzas,
                        apiMessage = "API indisponível. Catálogo local carregado."
                    )
                }
        }
    }

    fun placeOrder(pizza: Pizza) {
        viewModelScope.launch {
            runCatching { createOrderUseCase(pizza, 1) }
                .onSuccess { order ->
                    _uiState.value = _uiState.value.copy(
                        lastOrder = order,
                        apiMessage = "Pedido processado com sucesso."
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        apiMessage = "Erro ao processar pedido."
                    )
                }
        }
    }

    fun onUploadFileNameChange(value: String) {
        _uiState.value = _uiState.value.copy(uploadFileName = value)
    }

    fun uploadReceipt() {
        val fileName = uiState.value.uploadFileName
        viewModelScope.launch {
            runCatching { uploadReceiptUseCase(fileName) }
                .onSuccess { message ->
                    _uiState.value = _uiState.value.copy(receiptMessage = message)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(receiptMessage = "Falha no upload.")
                }
        }
    }

    fun refreshAll() {
        loadCatalog()
    }
}
