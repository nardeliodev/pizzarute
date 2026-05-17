package com.example.pizzarute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.pizzarute.presentation.ui.AuthScreen
import com.example.pizzarute.presentation.ui.HomeScreen
import com.example.pizzarute.presentation.ui.PizzaRuteTheme
import com.example.pizzarute.presentation.viewmodel.AuthUiState
import com.example.pizzarute.presentation.viewmodel.HomeViewModel
import com.example.pizzarute.presentation.viewmodel.HomeViewModelFactory

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (application as PizzaRuteApplication).appContainer
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            PizzaRuteTheme {

                var isLoggedIn by remember {
                    mutableStateOf(false)
                }

                var userLabel by remember {
                    mutableStateOf("Usuário")
                }

                if (isLoggedIn) {

                    HomeScreen(
                        userLabel = userLabel,
                        authMode = "Demo",
                        viewModel = homeViewModel,

                        onLogout = {
                            isLoggedIn = false
                        }
                    )

                } else {

                    AuthScreen(

                        uiState = AuthUiState(),

                        onLogin = { username, _ ->
                            userLabel = username
                            isLoggedIn = true
                        },

                        onSignUp = { username, _, _ ->
                            userLabel = username
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }
}