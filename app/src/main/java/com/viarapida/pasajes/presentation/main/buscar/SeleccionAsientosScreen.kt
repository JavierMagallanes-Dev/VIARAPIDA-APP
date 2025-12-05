package com.viarapida.pasajes.presentation.main.buscar

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import com.viarapida.pasajes.ui.theme.*

enum class EstadoAsiento {
    DISPONIBLE, OCUPADO, SELECCIONADO
}

data class Asiento(
    val numero: Int,
    val estado: EstadoAsiento,
    val fila: Int,
    val columna: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionAsientosScreen(
    destino: Destino,
    horario: Horario,
    onNavigateBack: () -> Unit,
    onContinuar: (Int) -> Unit
) {
    var asientoSeleccionado by remember { mutableStateOf<Int?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Generar asientos (45 asientos: 11 filas x 4 columnas)
    val asientos = remember {
        (1..45).map { numero ->
            val fila = (numero - 1) / 4
            val columna = (numero - 1) % 4
            val ocupados = listOf(1, 3, 7, 12, 15, 20, 23, 28, 35, 40)

            Asiento(
                numero = numero,
                estado = if (ocupados.contains(numero)) EstadoAsiento.OCUPADO else EstadoAsiento.DISPONIBLE,
                fila = fila,
                columna = columna
            )
        }
    }

    val asientosMutable = remember { mutableStateListOf<Asiento>().apply { addAll(asientos) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Selecciona tu asiento",
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
        },
        bottomBar = {
            if (asientoSeleccionado != null) {
                BottomBarSeleccionado(
                    numeroAsiento = asientoSeleccionado!!,
                    precio = destino.precio,
                    onContinuar = {
                        showConfirmDialog = true
                    }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Info del viaje compacta
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = destino.origen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                            Text(
                                text = horario.horaSalida,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = SecondaryOrange,
                            modifier = Modifier.size(28.dp)
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = destino.destino,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                            Text(
                                text = horario.horaLlegada,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Leyenda mejorada
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LeyendaItemMejorado(
                            color = Color.White,
                            borderColor = PrimaryBlue,
                            label = "Disponible",
                            icon = Icons.Default.CheckCircle
                        )
                        LeyendaItemMejorado(
                            color = TextDisabled,
                            borderColor = TextDisabled,
                            label = "Ocupado",
                            icon = Icons.Default.Cancel
                        )
                        LeyendaItemMejorado(
                            color = SecondaryOrange,
                            borderColor = SecondaryOrange,
                            label = "Seleccionado",
                            icon = Icons.Default.Star
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Mapa del bus mejorado
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Conductor
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            PrimaryBlue.copy(alpha = 0.15f),
                                            PrimaryBlue.copy(alpha = 0.25f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Conductor",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Conductor",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Asientos organizados por filas
                        val filas = asientosMutable.groupBy { it.fila }

                        filas.forEach { (fila, asientosEnFila) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                asientosEnFila.forEachIndexed { index, asiento ->
                                    AsientoItemMejorado(
                                        asiento = asiento,
                                        isSeleccionado = asiento.numero == asientoSeleccionado,
                                        onClick = {
                                            if (asiento.estado == EstadoAsiento.DISPONIBLE) {
                                                // Deseleccionar anterior
                                                asientoSeleccionado?.let { prevNumero ->
                                                    val prevIndex = asientosMutable.indexOfFirst { it.numero == prevNumero }
                                                    if (prevIndex != -1) {
                                                        asientosMutable[prevIndex] = asientosMutable[prevIndex].copy(
                                                            estado = EstadoAsiento.DISPONIBLE
                                                        )
                                                    }
                                                }

                                                // Seleccionar nuevo
                                                asientoSeleccionado = asiento.numero
                                                val currentIndex = asientosMutable.indexOf(asiento)
                                                asientosMutable[currentIndex] = asiento.copy(
                                                    estado = EstadoAsiento.SELECCIONADO
                                                )
                                            }
                                        }
                                    )

                                    // Pasillo después de 2 asientos
                                    if (index == 1) {
                                        Spacer(modifier = Modifier.width(32.dp))
                                    } else if (index < asientosEnFila.size - 1) {
                                        Spacer(modifier = Modifier.width(10.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(56.dp)
                )
            },
            title = {
                Text(
                    text = "Confirmar asiento",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Has seleccionado el asiento",
                        textAlign = TextAlign.Center,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nº $asientoSeleccionado",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Deseas continuar al pago?",
                        textAlign = TextAlign.Center,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        asientoSeleccionado?.let { onContinuar(it) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sí, continuar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun BottomBarSeleccionado(
    numeroAsiento: Int,
    precio: Double,
    onContinuar: () -> Unit
) {
    Surface(
        shadowElevation = 12.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(SecondaryOrange, Color(0xFFFF6F00))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.EventSeat,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Asiento seleccionado",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Nº $numeroAsiento",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "S/ ${precio.toInt()}.00",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SuccessGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryOrange
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = "Continuar al pago",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun AsientoItemMejorado(
    asiento: Asiento,
    isSeleccionado: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSeleccionado) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val backgroundColor = when {
        isSeleccionado -> SecondaryOrange
        asiento.estado == EstadoAsiento.OCUPADO -> TextDisabled
        else -> Color.White
    }

    val borderColor = when {
        isSeleccionado -> SecondaryOrange
        asiento.estado == EstadoAsiento.OCUPADO -> TextDisabled
        else -> PrimaryBlue
    }

    val textColor = when {
        isSeleccionado -> Color.White
        asiento.estado == EstadoAsiento.OCUPADO -> Color.White
        else -> TextPrimary
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = asiento.estado != EstadoAsiento.OCUPADO) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.EventSeat,
                contentDescription = "Asiento ${asiento.numero}",
                tint = textColor,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = asiento.numero.toString(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun LeyendaItemMejorado(
    color: Color,
    borderColor: Color,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}