package com.example.pizzarute.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pizzarute.di.AppContainer

class HomeViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                fetchCatalogUseCase = appContainer.fetchCatalogUseCase,
                createOrderUseCase = appContainer.createOrderUseCase,
                uploadReceiptUseCase = appContainer.uploadReceiptUseCase
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
