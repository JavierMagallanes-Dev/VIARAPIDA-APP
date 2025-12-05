package com.viarapida.pasajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.viarapida.pasajes.presentation.auth.login.LoginScreen
import com.viarapida.pasajes.presentation.auth.register.RegisterScreen
import com.viarapida.pasajes.presentation.main.home.HomeScreen
import com.viarapida.pasajes.presentation.main.buscar.BuscarScreen
import com.viarapida.pasajes.presentation.main.buscar.HorariosScreen
import com.viarapida.pasajes.presentation.main.buscar.PagoScreen
import com.viarapida.pasajes.presentation.main.buscar.ConfirmacionScreen
import com.viarapida.pasajes.presentation.splash.SplashScreen
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.viarapida.pasajes.ui.theme.ViaRapidaTheme
import com.viarapida.pasajes.presentation.main.buscar.SeleccionAsientosScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar Splash Screen nativo
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ViaRapidaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ViaRapidaApp()
                }
            }
        }
    }
}

@Composable
fun ViaRapidaApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Register Screen
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable("home") {
            HomeScreen(
                onNavigateToBuscar = {
                    navController.navigate("buscar")
                },
                onNavigateToMisViajes = {
                    // TODO: Navegar a mis viajes
                },
                onNavigateToPerfil = {
                    // TODO: Navegar a perfil
                }
            )
        }

        // Buscar Screen
        composable("buscar") {
            BuscarScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onDestinoSelected = { destino ->
                    val destinoJson = Gson().toJson(destino)
                    val encodedDestino = URLEncoder.encode(destinoJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("horarios/$encodedDestino")
                }
            )
        }

        // Horarios Screen
        composable(
            route = "horarios/{destinoJson}",
            arguments = listOf(navArgument("destinoJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val destinoJson = backStackEntry.arguments?.getString("destinoJson")
            val decodedJson = URLDecoder.decode(destinoJson, StandardCharsets.UTF_8.toString())
            val destino = Gson().fromJson(decodedJson, Destino::class.java)

            HorariosScreen(
                destino = destino,
                onNavigateBack = { navController.popBackStack() },
                onHorarioSelected = { horario ->
                    val horarioJson = Gson().toJson(horario)
                    val encodedHorario = URLEncoder.encode(horarioJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("seleccion_asientos/$destinoJson/$encodedHorario")
                }
            )
        }

        // Selección de Asientos Screen
        composable(
            route = "seleccion_asientos/{destinoJson}/{horarioJson}",
            arguments = listOf(
                navArgument("destinoJson") { type = NavType.StringType },
                navArgument("horarioJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val destinoJson = backStackEntry.arguments?.getString("destinoJson")
            val horarioJson = backStackEntry.arguments?.getString("horarioJson")

            val decodedDestinoJson = URLDecoder.decode(destinoJson, StandardCharsets.UTF_8.toString())
            val decodedHorarioJson = URLDecoder.decode(horarioJson, StandardCharsets.UTF_8.toString())

            val destino = Gson().fromJson(decodedDestinoJson, Destino::class.java)
            val horario = Gson().fromJson(decodedHorarioJson, Horario::class.java)

            SeleccionAsientosScreen(
                destino = destino,
                horario = horario,
                onNavigateBack = { navController.popBackStack() },
                onContinuar = { numeroAsiento ->
                    navController.navigate("pago/$destinoJson/$horarioJson/$numeroAsiento")
                }
            )
        }

        // Pago Screen
        composable(
            route = "pago/{destinoJson}/{horarioJson}/{numeroAsiento}",
            arguments = listOf(
                navArgument("destinoJson") { type = NavType.StringType },
                navArgument("horarioJson") { type = NavType.StringType },
                navArgument("numeroAsiento") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val destinoJson = backStackEntry.arguments?.getString("destinoJson")
            val horarioJson = backStackEntry.arguments?.getString("horarioJson")
            val numeroAsiento = backStackEntry.arguments?.getInt("numeroAsiento") ?: 0

            val decodedDestinoJson = URLDecoder.decode(destinoJson, StandardCharsets.UTF_8.toString())
            val decodedHorarioJson = URLDecoder.decode(horarioJson, StandardCharsets.UTF_8.toString())

            val destino = Gson().fromJson(decodedDestinoJson, Destino::class.java)
            val horario = Gson().fromJson(decodedHorarioJson, Horario::class.java)

            PagoScreen(
                destino = destino,
                horario = horario,
                numeroAsiento = numeroAsiento,
                onNavigateBack = { navController.popBackStack() },
                onPagoCompletado = {
                    navController.navigate("confirmacion/$destinoJson/$horarioJson/$numeroAsiento") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        // Confirmación Screen
        composable(
            route = "confirmacion/{destinoJson}/{horarioJson}/{numeroAsiento}",
            arguments = listOf(
                navArgument("destinoJson") { type = NavType.StringType },
                navArgument("horarioJson") { type = NavType.StringType },
                navArgument("numeroAsiento") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val destinoJson = backStackEntry.arguments?.getString("destinoJson")
            val horarioJson = backStackEntry.arguments?.getString("horarioJson")
            val numeroAsiento = backStackEntry.arguments?.getInt("numeroAsiento") ?: 0

            val decodedDestinoJson = URLDecoder.decode(destinoJson, StandardCharsets.UTF_8.toString())
            val decodedHorarioJson = URLDecoder.decode(horarioJson, StandardCharsets.UTF_8.toString())

            val destino = Gson().fromJson(decodedDestinoJson, Destino::class.java)
            val horario = Gson().fromJson(decodedHorarioJson, Horario::class.java)

            ConfirmacionScreen(
                destino = destino,
                horario = horario,
                numeroAsiento = numeroAsiento,
                onVolverAlInicio = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onDescargarPasaje = {
                    // TODO: Implementar descarga de PDF
                }
            )
        }
    }
}