package com.example.pizzarute.domain.usecase

import com.example.pizzarute.domain.repository.PizzaRepository

class UploadReceiptUseCase(
    private val repository: PizzaRepository
) {
    suspend operator fun invoke(fileName: String): String {
        return repository.uploadReceipt(fileName)
    }
}
