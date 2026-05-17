package com.example.pizzarute.data.remote

import android.content.Context
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.StoragePath
import com.example.pizzarute.domain.model.Order
import com.example.pizzarute.domain.model.Pizza
import com.example.pizzarute.util.CatalogFallback
import com.example.pizzarute.util.JsonParser
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.util.UUID
import kotlin.coroutines.resume

class AmplifyPizzaRemoteDataSource(
    private val context: Context
) {

    suspend fun fetchCatalog(): List<Pizza> = suspendCancellableCoroutine { continuation ->
        val request = RestOptions.builder()
            .addPath("/pizzas")
            .build()

        try {
            // AWS Amplify API: leitura do catálogo via endpoint REST (API Gateway/backend configurado).
            Amplify.API.get(
                request,
                { response ->
                    val parsed = JsonParser.parseCatalog(response.data.toString())
                    continuation.resume(
                        if (parsed.isNotEmpty()) parsed else CatalogFallback.pizzas
                    )
                },
                {
                    continuation.resume(CatalogFallback.pizzas)
                }
            )
        } catch (_: Throwable) {
            continuation.resume(CatalogFallback.pizzas)
        }
    }

    suspend fun createOrder(pizza: Pizza, quantity: Int): Order = suspendCancellableCoroutine { continuation ->
        val total = pizza.price * quantity

        val body = """
            {
              "pizzaId": "${pizza.id}",
              "name": "${pizza.name}",
              "quantity": $quantity,
              "total": $total
            }
        """.trimIndent().toByteArray()

        val request = RestOptions.builder()
            .addPath("/pedidos")
            .addBody(body)
            .build()

        try {
            // AWS Amplify API: envio do pedido para o endpoint REST do backend.
            Amplify.API.post(
                request,
                {
                    continuation.resume(
                        Order(
                            id = UUID.randomUUID().toString().take(8),
                            pizza = pizza,
                            quantity = quantity,
                            total = total,
                            status = "Pedido enviado"
                        )
                    )
                },
                {
                    continuation.resume(
                        Order(
                            id = UUID.randomUUID().toString().take(8),
                            pizza = pizza,
                            quantity = quantity,
                            total = total,
                            status = "Modo demo ativo"
                        )
                    )
                }
            )
        } catch (_: Throwable) {
            continuation.resume(
                Order(
                    id = UUID.randomUUID().toString().take(8),
                    pizza = pizza,
                    quantity = quantity,
                    total = total,
                    status = "Modo demo ativo"
                )
            )
        }
    }

    suspend fun uploadReceipt(fileName: String): String = suspendCancellableCoroutine { continuation ->
        val safeName = if (fileName.isBlank()) {
            "comprovante_${System.currentTimeMillis()}.txt"
        } else {
            fileName
        }

        val file = File(context.filesDir, safeName).apply {
            writeText("Comprovante Pizza Rute - ${System.currentTimeMillis()}")
        }

        val options = StorageUploadFileOptions.builder().build()

        try {
            // AWS Amplify Storage: upload do comprovante para o bucket S3.
            Amplify.Storage.uploadFile(
                StoragePath.fromString("public/comprovantes/$safeName"),
                file,
                options,
                { result ->
                    continuation.resume("Arquivo enviado para Storage: ${result.path}")
                },
                {
                    continuation.resume("Upload em modo demo concluído: $safeName")
                }
            )
        } catch (_: Throwable) {
            continuation.resume("Upload em modo demo concluído: $safeName")
        }
    }
}
