package com.viarapida.pasajes.presentation.main.buscar

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import com.viarapida.pasajes.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorariosScreen(
    destino: Destino,
    onNavigateBack: () -> Unit,
    onHorarioSelected: (Horario) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    // Simular carga de horarios
    LaunchedEffect(Unit) {
        delay(800)
        isLoading = false
    }

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
                title = {
                    Text(
                        "Horarios Disponibles",
                        fontWeight = FontWeight.Bold
                    )
                },
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
            // Información del destino con diseño mejorado
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box {
                        // Gradiente superior
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(PrimaryBlue, SecondaryOrange)
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Spacer(modifier = Modifier.height(6.dp))

                            // Ruta principal
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Origen
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "Salida",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = destino.origen,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PrimaryBlue
                                    )
                                }

                                // Flecha animada
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            color = SecondaryOrange.copy(alpha = 0.15f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        tint = SecondaryOrange,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Destino
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Llegada",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = destino.destino,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PrimaryBlue
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Divider(
                                color = DividerColor.copy(alpha = 0.3f),
                                thickness = 1.dp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Información del viaje
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                InfoChipDestino(
                                    icon = Icons.Default.Schedule,
                                    label = "Duración",
                                    value = destino.duracionFormateada(),
                                    color = InfoBlue
                                )

                                InfoChipDestino(
                                    icon = Icons.Default.Route,
                                    label = "Distancia",
                                    value = "${destino.distanciaKm.toInt()} km",
                                    color = SuccessGreen
                                )

                                InfoChipDestino(
                                    icon = Icons.Default.AttachMoney,
                                    label = "Desde",
                                    value = "S/ ${destino.precio.toInt()}",
                                    color = SecondaryOrange
                                )
                            }
                        }
                    }
                }
            }

            // Título de horarios
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Selecciona tu horario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${horarios.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }

            // Loading o Lista de horarios
            if (isLoading) {
                items(3) {
                    ShimmerHorarioCard()
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                items(horarios) { horario ->
                    HorarioCardMejorado(
                        horario = horario,
                        destino = destino,
                        onClick = { onHorarioSelected(horario) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InfoChipDestino(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
fun HorarioCardMejorado(
    horario: Horario,
    destino: Destino,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Tipo de bus y disponibilidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        PrimaryBlue.copy(alpha = 0.2f),
                                        PrimaryBlueDark.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DirectionsBus,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = horario.tipoBus,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.EventSeat,
                                contentDescription = null,
                                tint = if (horario.asientosDisponibles < 10) ErrorRed else SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${horario.asientosDisponibles} disponibles",
                                fontSize = 12.sp,
                                color = if (horario.asientosDisponibles < 10) ErrorRed else SuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Indicador circular de ocupación
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { (horario.asientosTotales - horario.asientosDisponibles).toFloat() / horario.asientosTotales },
                        modifier = Modifier.fillMaxSize(),
                        color = when {
                            horario.porcentajeOcupacion() < 50 -> SuccessGreen
                            horario.porcentajeOcupacion() < 80 -> WarningYellow
                            else -> ErrorRed
                        },
                        strokeWidth = 5.dp,
                        trackColor = Color.LightGray.copy(alpha = 0.2f),
                    )
                    Text(
                        text = "${horario.porcentajeOcupacion()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            horario.porcentajeOcupacion() < 50 -> SuccessGreen
                            horario.porcentajeOcupacion() < 80 -> Color(0xFFF57C00)
                            else -> ErrorRed
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horarios con diseño mejorado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hora de salida
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = PrimaryBlue.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Salida",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = horario.horaSalida,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryBlue
                    )
                }

                // Duración
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.8f)
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = destino.duracionFormateada(),
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Hora de llegada
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = SecondaryOrange.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Llegada",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = horario.horaLlegada,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SecondaryOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider(color = DividerColor.copy(alpha = 0.3f))

            Spacer(modifier = Modifier.height(20.dp))

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
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "S/",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Text(
                            text = " ${destino.precio.toInt()}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen
                        )
                        Text(
                            text = ".00",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryOrange
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    ),
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Seleccionar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerHorarioCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = alpha))
        )
    }
}