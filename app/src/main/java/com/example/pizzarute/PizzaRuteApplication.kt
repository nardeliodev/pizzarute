package com.example.pizzarute

import android.app.Application
import android.util.Log
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.configuration.AmplifyOutputs
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.example.pizzarute.di.AppContainer

class PizzaRuteApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(applicationContext)

        try {
            // AWS Amplify Auth: autenticação com Cognito para login, cadastro e logout.
            Amplify.addPlugin(AWSCognitoAuthPlugin())

            // AWS Amplify Storage: envio de arquivos comprovantes para o bucket S3.
            Amplify.addPlugin(AWSS3StoragePlugin())

            // AWS Amplify API: integração com a camada REST usada pelo catálogo e pedidos.
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(AmplifyOutputs(R.raw.amplify_outputs), applicationContext)
            Log.i(TAG, "Amplify inicializado com Auth, Storage e API.")
        } catch (error: Exception) {
            Log.e(TAG, "Falha ao inicializar Amplify.", error)
        }
    }

    companion object {
        private const val TAG = "PizzaRuteApplication"
    }
}