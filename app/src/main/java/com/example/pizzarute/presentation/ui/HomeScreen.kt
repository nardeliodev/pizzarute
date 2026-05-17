package com.example.pizzarute.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pizzarute.domain.model.Order
import com.example.pizzarute.domain.model.Pizza
import com.example.pizzarute.presentation.viewmodel.HomeTab
import com.example.pizzarute.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userLabel: String,
    authMode: String,
    viewModel: HomeViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    // AWS Amplify API Gateway
    // Atualiza os dados vindos da API ao abrir a tela
    LaunchedEffect(Unit) {
        viewModel.refreshAll()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    PizzaRuteToolbarTitle()
                },
                actions = {
                    TextButton(
                        onClick = onLogout
                    ) {
                        Text(
                            text = "Sair",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bem-vindo, $userLabel",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // AWS Amplify Auth
                    // Exibe o tipo de autenticação do usuário
                    Text("Modo de autenticação: $authMode")

                    // AWS Amplify
                    // Integração com Auth + Storage + API Gateway
                    Text("Autenticação, Storage e API em um único app.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab
            ) {
                HomeTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(tab.title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (HomeTab.entries[selectedTab]) {
                HomeTab.MENU -> {
                    CatalogTab(
                        loading = uiState.isLoading,
                        apiMessage = uiState.apiMessage,
                        catalog = uiState.catalog,
                        onOrder = {
                            viewModel.placeOrder(it)
                        }
                    )
                }

                HomeTab.STORAGE -> {
                    StorageTab(
                        fileName = uiState.uploadFileName,
                        onFileNameChange = viewModel::onUploadFileNameChange,
                        receiptMessage = uiState.receiptMessage,
                        onUpload = viewModel::uploadReceipt
                    )
                }

                HomeTab.STATUS -> {
                    StatusTab(
                        apiMessage = uiState.apiMessage,
                        lastOrder = uiState.lastOrder,
                        onRefresh = viewModel::refreshAll
                    )
                }
            }
        }
    }
}

@Composable
private fun CatalogTab(
    loading: Boolean,
    apiMessage: String,
    catalog: List<Pizza>,
    onOrder: (Pizza) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 420.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Cardápio do dia",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // AWS API Gateway
        // Mensagem retornada da API
        Text(apiMessage)

        Spacer(modifier = Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            catalog.forEach { pizza ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            pizza.name,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(pizza.description)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            "Ingredientes: ${pizza.ingredients.joinToString()}"
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            "R$ %.2f".format(pizza.price)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onOrder(pizza) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Fazer pedido")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StorageTab(
    fileName: String,
    onFileNameChange: (String) -> Unit,
    receiptMessage: String,
    onUpload: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Envio de comprovante",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // AWS Amplify Storage
        // Simulação de upload para Amazon S3
        Text("Simulação de upload para Amazon S3 via Amplify Storage.")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fileName,
            onValueChange = onFileNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Nome do arquivo")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onUpload,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Enviar para Storage")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(receiptMessage)
    }
}

@Composable
private fun StatusTab(
    apiMessage: String,
    lastOrder: Order?,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Status da integração",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // AWS API Gateway
                Text("API: $apiMessage")

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRefresh,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Atualizar cardápio")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Último pedido", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                if (lastOrder == null) {
                    Text("Nenhum pedido realizado ainda.")
                } else {
                    Text("Pedido: ${lastOrder.id}")
                    Text("Pizza: ${lastOrder.pizza.name}")
                    Text("Quantidade: ${lastOrder.quantity}")
                    Text("Total: R$ %.2f".format(lastOrder.total))
                    Text("Status: ${lastOrder.status}")
                }
            }
        }
    }
}