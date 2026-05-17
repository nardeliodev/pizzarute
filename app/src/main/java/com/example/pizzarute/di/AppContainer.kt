package com.example.pizzarute.di

import android.content.Context
import com.example.pizzarute.data.remote.AmplifyAuthDataSource
import com.example.pizzarute.data.remote.AmplifyPizzaRemoteDataSource
import com.example.pizzarute.data.repository.AuthRepositoryImpl
import com.example.pizzarute.data.repository.PizzaRepositoryImpl
import com.example.pizzarute.domain.repository.AuthRepository
import com.example.pizzarute.domain.repository.PizzaRepository
import com.example.pizzarute.domain.usecase.CreateOrderUseCase
import com.example.pizzarute.domain.usecase.FetchCatalogUseCase
import com.example.pizzarute.domain.usecase.SignInUseCase
import com.example.pizzarute.domain.usecase.SignUpUseCase
import com.example.pizzarute.domain.usecase.UploadReceiptUseCase
import com.example.pizzarute.domain.usecase.LogoutUseCase

class AppContainer(appContext: Context) {

    // AUTH

    private val authDataSource =
        AmplifyAuthDataSource(appContext)

    private val authRepository: AuthRepository =
        AuthRepositoryImpl(authDataSource)

    val signInUseCase =
        SignInUseCase(authRepository)

    val signUpUseCase =
        SignUpUseCase(authRepository)

    val logoutUseCase =
        LogoutUseCase(authRepository)

    // PIZZA

    private val pizzaRemoteDataSource =
        AmplifyPizzaRemoteDataSource(appContext)

    private val pizzaRepository: PizzaRepository =
        PizzaRepositoryImpl(pizzaRemoteDataSource)

    val fetchCatalogUseCase =
        FetchCatalogUseCase(pizzaRepository)

    val createOrderUseCase =
        CreateOrderUseCase(pizzaRepository)

    val uploadReceiptUseCase =
        UploadReceiptUseCase(pizzaRepository)
}