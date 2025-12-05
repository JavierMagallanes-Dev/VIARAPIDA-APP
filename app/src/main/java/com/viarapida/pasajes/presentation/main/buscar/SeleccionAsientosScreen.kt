package com.viarapida.pasajes.presentation.main.buscar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Generar asientos (45 asientos en total: 11 filas x 4 columnas + pasillo)
    val asientos = remember {
        (1..45).map { numero ->
            val fila = (numero - 1) / 4
            val columna = (numero - 1) % 4

            // Simular algunos asientos ocupados
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
                title = { Text("Selecciona tu asiento") },
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
                Surface(
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Asiento seleccionado",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Nº $asientoSeleccionado",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total a pagar",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = destino.precioFormateado(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { asientoSeleccionado?.let { onContinuar(it) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryOrange)
                        ) {
                            Text(
                                text = "Continuar al pago",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
        ) {
            // Información del viaje
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = destino.origen,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = horario.horaSalida,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }

                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = SecondaryOrange
                            )

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = destino.destino,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = horario.horaLlegada,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                color = PrimaryBlueLight.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.DirectionsBus,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = horario.tipoBus,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = PrimaryBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Leyenda
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LeyendaItem(
                            color = Color.White,
                            borderColor = TextSecondary,
                            label = "Disponible"
                        )
                        LeyendaItem(
                            color = TextDisabled,
                            borderColor = TextDisabled,
                            label = "Ocupado"
                        )
                        LeyendaItem(
                            color = SecondaryOrange,
                            borderColor = SecondaryOrange,
                            label = "Seleccionado"
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Mapa del bus
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título del conductor
                        Surface(
                            color = PrimaryBlueLight.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Conductor",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Conductor",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = PrimaryBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Asientos organizados por filas
                        val filas = asientosMutable.groupBy { it.fila }

                        filas.forEach { (fila, asientosEnFila) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                asientosEnFila.forEachIndexed { index, asiento ->
                                    AsientoItem(
                                        asiento = asiento,
                                        isSeleccionado = asiento.numero == asientoSeleccionado,
                                        onClick = {
                                            if (asiento.estado == EstadoAsiento.DISPONIBLE) {
                                                // Deseleccionar asiento anterior
                                                asientoSeleccionado?.let { prevNumero ->
                                                    val prevIndex = asientosMutable.indexOfFirst { it.numero == prevNumero }
                                                    if (prevIndex != -1) {
                                                        asientosMutable[prevIndex] = asientosMutable[prevIndex].copy(
                                                            estado = EstadoAsiento.DISPONIBLE
                                                        )
                                                    }
                                                }

                                                // Seleccionar nuevo asiento
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
                                        Spacer(modifier = Modifier.width(24.dp))
                                    } else if (index < asientosEnFila.size - 1) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun AsientoItem(
    asiento: Asiento,
    isSeleccionado: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSeleccionado -> SecondaryOrange
        asiento.estado == EstadoAsiento.OCUPADO -> TextDisabled
        else -> Color.White
    }

    val borderColor = when {
        isSeleccionado -> SecondaryOrange
        asiento.estado == EstadoAsiento.OCUPADO -> TextDisabled
        else -> TextSecondary
    }

    val textColor = when {
        isSeleccionado -> Color.White
        asiento.estado == EstadoAsiento.OCUPADO -> Color.White
        else -> TextPrimary
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = asiento.estado != EstadoAsiento.OCUPADO) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.EventSeat,
                contentDescription = "Asiento ${asiento.numero}",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = asiento.numero.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun LeyendaItem(
    color: Color,
    borderColor: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(4.dp)
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}