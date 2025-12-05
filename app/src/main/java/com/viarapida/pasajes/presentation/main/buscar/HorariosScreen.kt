package com.viarapida.pasajes.presentation.main.buscar

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
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import com.viarapida.pasajes.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorariosScreen(
    destino: Destino,
    onNavigateBack: () -> Unit,
    onHorarioSelected: (Horario) -> Unit
) {
    // Horarios de prueba
    val horarios = remember {
        listOf(
            Horario(
                id = "h1",
                destinoId = destino.id,
                fecha = Timestamp.now(),
                horaSalida = "06:00 AM",
                horaLlegada = "03:00 PM",
                asientosDisponibles = 35,
                asientosTotales = 45,
                tipoBus = "VIP",
                estado = "activo"
            ),
            Horario(
                id = "h2",
                destinoId = destino.id,
                fecha = Timestamp.now(),
                horaSalida = "08:30 AM",
                horaLlegada = "05:30 PM",
                asientosDisponibles = 20,
                asientosTotales = 45,
                tipoBus = "Semi-Cama",
                estado = "activo"
            ),
            Horario(
                id = "h3",
                destinoId = destino.id,
                fecha = Timestamp.now(),
                horaSalida = "10:00 AM",
                horaLlegada = "07:00 PM",
                asientosDisponibles = 42,
                asientosTotales = 45,
                tipoBus = "Estándar",
                estado = "activo"
            ),
            Horario(
                id = "h4",
                destinoId = destino.id,
                fecha = Timestamp.now(),
                horaSalida = "02:00 PM",
                horaLlegada = "11:00 PM",
                asientosDisponibles = 10,
                asientosTotales = 45,
                tipoBus = "VIP",
                estado = "activo"
            ),
            Horario(
                id = "h5",
                destinoId = destino.id,
                fecha = Timestamp.now(),
                horaSalida = "06:00 PM",
                horaLlegada = "03:00 AM",
                asientosDisponibles = 28,
                asientosTotales = 45,
                tipoBus = "Semi-Cama",
                estado = "activo"
            ),
            Horario(
                id = "h6",
                destinoId = destino.id,
                fecha = Timestamp.now(),
                horaSalida = "10:00 PM",
                horaLlegada = "07:00 AM",
                asientosDisponibles = 5,
                asientosTotales = 45,
                tipoBus = "VIP",
                estado = "activo"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Horarios Disponibles") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
        ) {
            // Información del destino
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = destino.origen,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "Origen",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = SecondaryOrange,
                                modifier = Modifier.size(32.dp)
                            )

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = destino.destino,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "Destino",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(color = DividerColor)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            InfoChip(
                                icon = Icons.Default.Schedule,
                                label = "Duración",
                                value = destino.duracionFormateada()
                            )
                            InfoChip(
                                icon = Icons.Default.Route,
                                label = "Distancia",
                                value = "${destino.distanciaKm.toInt()} km"
                            )
                            InfoChip(
                                icon = Icons.Default.AttachMoney,
                                label = "Desde",
                                value = destino.precioFormateado()
                            )
                        }
                    }
                }
            }

            // Título de horarios
            item {
                Text(
                    text = "Selecciona tu horario",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Lista de horarios
            items(horarios) { horario ->
                HorarioCard(
                    horario = horario,
                    destino = destino,
                    onClick = { onHorarioSelected(horario) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = PrimaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
fun HorarioCard(
    horario: Horario,
    destino: Destino,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Tipo de bus y disponibilidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = horario.tipoBus,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${horario.asientosDisponibles} asientos disponibles",
                            fontSize = 12.sp,
                            color = if (horario.asientosDisponibles < 10) ErrorRed else SuccessGreen
                        )
                    }
                }

                // Indicador de ocupación
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = when {
                                horario.porcentajeOcupacion() < 50 -> SuccessGreen.copy(alpha = 0.2f)
                                horario.porcentajeOcupacion() < 80 -> WarningYellow.copy(alpha = 0.2f)
                                else -> ErrorRed.copy(alpha = 0.2f)
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${horario.porcentajeOcupacion()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            horario.porcentajeOcupacion() < 50 -> SuccessGreen
                            horario.porcentajeOcupacion() < 80 -> Color(0xFFF57C00)
                            else -> ErrorRed
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horarios
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = horario.horaSalida,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Text(
                        text = "Salida",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                    Text(
                        text = destino.duracionFormateada(),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = horario.horaLlegada,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Text(
                        text = "Llegada",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = DividerColor)

            Spacer(modifier = Modifier.height(16.dp))

            // Precio y botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Precio por persona",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = destino.precioFormateado(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryOrange
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Seleccionar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}