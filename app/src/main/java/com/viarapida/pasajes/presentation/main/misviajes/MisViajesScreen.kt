package com.viarapida.pasajes.presentation.main.misviajes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.viarapida.pasajes.data.model.Pasaje
import com.viarapida.pasajes.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisViajesScreen(
    onNavigateBack: () -> Unit,
    onPasajeClick: (Pasaje) -> Unit
) {
    var tabSeleccionado by remember { mutableStateOf(0) }

    // Pasajes de prueba
    val pasajesProximos = remember {
        listOf(
            Pasaje(
                id = "p1",
                usuarioId = "user1",
                horarioId = "h1",
                numeroAsiento = 12,
                codigoQR = "VR-2024-001",
                precioTotal = 45.0,
                metodoPago = "tarjeta",
                estado = "pagado",
                fechaCompra = Timestamp.now(),
                fechaViaje = Timestamp(Date(System.currentTimeMillis() + 86400000)), // Mañana
                usuarioNombre = "Juan Pérez",
                origenDestino = "Ayacucho → Lima",
                horaSalida = "06:00 AM"
            ),
            Pasaje(
                id = "p2",
                usuarioId = "user1",
                horarioId = "h2",
                numeroAsiento = 25,
                codigoQR = "VR-2024-002",
                precioTotal = 30.0,
                metodoPago = "yape",
                estado = "pagado",
                fechaCompra = Timestamp.now(),
                fechaViaje = Timestamp(Date(System.currentTimeMillis() + 172800000)), // En 2 días
                usuarioNombre = "Juan Pérez",
                origenDestino = "Ayacucho → Huancayo",
                horaSalida = "08:30 AM"
            )
        )
    }

    val pasajesHistorial = remember {
        listOf(
            Pasaje(
                id = "p3",
                usuarioId = "user1",
                horarioId = "h3",
                numeroAsiento = 8,
                codigoQR = "VR-2023-045",
                precioTotal = 55.0,
                metodoPago = "efectivo",
                estado = "usado",
                fechaCompra = Timestamp(Date(System.currentTimeMillis() - 604800000)),
                fechaViaje = Timestamp(Date(System.currentTimeMillis() - 259200000)), // Hace 3 días
                usuarioNombre = "Juan Pérez",
                origenDestino = "Ayacucho → Cusco",
                horaSalida = "10:00 AM"
            ),
            Pasaje(
                id = "p4",
                usuarioId = "user1",
                horarioId = "h4",
                numeroAsiento = 15,
                codigoQR = "VR-2023-038",
                precioTotal = 45.0,
                metodoPago = "tarjeta",
                estado = "usado",
                fechaCompra = Timestamp(Date(System.currentTimeMillis() - 1209600000)),
                fechaViaje = Timestamp(Date(System.currentTimeMillis() - 864000000)), // Hace 10 días
                usuarioNombre = "Juan Pérez",
                origenDestino = "Lima → Ayacucho",
                horaSalida = "07:00 PM"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Viajes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = tabSeleccionado,
                containerColor = Color.White,
                contentColor = PrimaryBlue
            ) {
                Tab(
                    selected = tabSeleccionado == 0,
                    onClick = { tabSeleccionado = 0 },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = "Próximos"
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Próximos")
                        }
                    }
                )
                Tab(
                    selected = tabSeleccionado == 1,
                    onClick = { tabSeleccionado = 1 },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = "Historial"
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Historial")
                        }
                    }
                )
            }

            // Contenido según tab
            when (tabSeleccionado) {
                0 -> {
                    if (pasajesProximos.isEmpty()) {
                        EmptyState(
                            icono = Icons.Default.ConfirmationNumber,
                            titulo = "No tienes viajes próximos",
                            mensaje = "Cuando reserves un pasaje aparecerá aquí"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(pasajesProximos) { pasaje ->
                                PasajeCard(
                                    pasaje = pasaje,
                                    onClick = { onPasajeClick(pasaje) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    if (pasajesHistorial.isEmpty()) {
                        EmptyState(
                            icono = Icons.Default.History,
                            titulo = "No tienes historial",
                            mensaje = "Tus viajes completados aparecerán aquí"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(pasajesHistorial) { pasaje ->
                                PasajeCard(
                                    pasaje = pasaje,
                                    onClick = { onPasajeClick(pasaje) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PasajeCard(
    pasaje: Pasaje,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pasaje.origenDestino,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Surface(
                    color = Color(pasaje.getEstadoColor()).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = pasaje.getEstadoTexto(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(pasaje.getEstadoColor()),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha y hora
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pasaje.fechaViajeFormateada(),
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pasaje.horaSalida,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = DividerColor)

            Spacer(modifier = Modifier.height(12.dp))

            // Detalles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.EventSeat,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Asiento ${pasaje.numeroAsiento}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }

                Text(
                    text = pasaje.precioFormateado(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Código de reserva
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.QrCode2,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pasaje.codigoQR,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                if (pasaje.estado == "pagado") {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Ver detalles",
                        tint = PrimaryBlue
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    mensaje: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = TextSecondary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = titulo,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mensaje,
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}