package com.viarapida.pasajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import com.viarapida.pasajes.data.model.Pasaje
import com.viarapida.pasajes.presentation.auth.login.LoginScreen
import com.viarapida.pasajes.presentation.auth.register.RegisterScreen
import com.viarapida.pasajes.presentation.main.buscar.BuscarScreen
import com.viarapida.pasajes.presentation.main.buscar.ConfirmacionScreen
import com.viarapida.pasajes.presentation.main.buscar.HorariosScreen
import com.viarapida.pasajes.presentation.main.buscar.PagoScreen
import com.viarapida.pasajes.presentation.main.buscar.SeleccionAsientosScreen
import com.viarapida.pasajes.presentation.main.home.HomeScreen
import com.viarapida.pasajes.presentation.main.misviajes.DetallePasajeScreen
import com.viarapida.pasajes.presentation.main.misviajes.MisViajesScreen
import com.viarapida.pasajes.presentation.main.perfil.PerfilScreen
import com.viarapida.pasajes.presentation.splash.SplashScreen as AppSplashScreen
import com.viarapida.pasajes.ui.theme.ViaRapidaTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ViaRapidaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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
        composable("splash") {
            AppSplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToBuscar = { navController.navigate("buscar") },
                onNavigateToMisViajes = { navController.navigate("mis_viajes") },
                onNavigateToPerfil = { navController.navigate("perfil") }
            )
        }

        composable("buscar") {
            BuscarScreen(
                onNavigateBack = { navController.popBackStack() },
                onDestinoSelected = { destino ->
                    val destinoJson = Gson().toJson(destino)
                    val encodedDestino = URLEncoder.encode(destinoJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("horarios/$encodedDestino")
                }
            )
        }

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
                onDescargarPasaje = { }
            )
        }

        composable("mis_viajes") {
            MisViajesScreen(
                onNavigateBack = { navController.popBackStack() },
                onPasajeClick = { pasaje ->
                    val pasajeJson = Gson().toJson(pasaje)
                    val encodedPasaje = URLEncoder.encode(pasajeJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("detalle_pasaje/$encodedPasaje")
                }
            )
        }

        composable(
            route = "detalle_pasaje/{pasajeJson}",
            arguments = listOf(navArgument("pasajeJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val pasajeJson = backStackEntry.arguments?.getString("pasajeJson")
            val decodedJson = URLDecoder.decode(pasajeJson, StandardCharsets.UTF_8.toString())
            val pasaje = Gson().fromJson(decodedJson, Pasaje::class.java)

            DetallePasajeScreen(
                pasaje = pasaje,
                onNavigateBack = { navController.popBackStack() },
                onDescargar = { },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable("perfil") {
            PerfilScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditarPerfil = {
                    // TODO: Implementar edición de perfil
                },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}