package com.viarapida.pasajes.presentation.main.buscar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import com.viarapida.pasajes.ui.theme.*

enum class MetodoPago {
    TARJETA, YAPE, PLIN, EFECTIVO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    destino: Destino,
    horario: Horario,
    numeroAsiento: Int,
    onNavigateBack: () -> Unit,
    onPagoCompletado: () -> Unit
) {
    var metodoPagoSeleccionado by remember { mutableStateOf<MetodoPago?>(null) }
    var showDialogPago by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    // Campos para tarjeta
    var numeroTarjeta by remember { mutableStateOf("") }
    var nombreTitular by remember { mutableStateOf("") }
    var fechaVencimiento by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // Campos para Yape/Plin
    var numeroCelular by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar pago") },
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
            // Resumen del viaje
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
                        Text(
                            text = "Resumen del viaje",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = destino.origen,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
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
                                tint = SecondaryOrange,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = destino.destino,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = horario.horaLlegada,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = DividerColor)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Detalles
                        DetalleItem(label = "Fecha", valor = horario.fechaFormateada())
                        Spacer(modifier = Modifier.height(8.dp))
                        DetalleItem(label = "Tipo de bus", valor = horario.tipoBus)
                        Spacer(modifier = Modifier.height(8.dp))
                        DetalleItem(label = "Asiento", valor = "Nº $numeroAsiento")
                        Spacer(modifier = Modifier.height(8.dp))
                        DetalleItem(label = "Duración", valor = destino.duracionFormateada())

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = DividerColor)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total a pagar",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = destino.precioFormateado(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }

            // Métodos de pago
            item {
                Text(
                    text = "Selecciona tu método de pago",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetodoPagoCard(
                        titulo = "Tarjeta de Crédito/Débito",
                        icono = Icons.Default.CreditCard,
                        isSelected = metodoPagoSeleccionado == MetodoPago.TARJETA,
                        onClick = { metodoPagoSeleccionado = MetodoPago.TARJETA }
                    )

                    MetodoPagoCard(
                        titulo = "Yape",
                        icono = Icons.Default.Phone,
                        isSelected = metodoPagoSeleccionado == MetodoPago.YAPE,
                        onClick = { metodoPagoSeleccionado = MetodoPago.YAPE }
                    )

                    MetodoPagoCard(
                        titulo = "Plin",
                        icono = Icons.Default.PhoneAndroid,
                        isSelected = metodoPagoSeleccionado == MetodoPago.PLIN,
                        onClick = { metodoPagoSeleccionado = MetodoPago.PLIN }
                    )

                    MetodoPagoCard(
                        titulo = "Efectivo en agencia",
                        icono = Icons.Default.AttachMoney,
                        isSelected = metodoPagoSeleccionado == MetodoPago.EFECTIVO,
                        onClick = { metodoPagoSeleccionado = MetodoPago.EFECTIVO }
                    )
                }
            }

            // Formulario según método de pago
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            when (metodoPagoSeleccionado) {
                MetodoPago.TARJETA -> {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Datos de la tarjeta",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = numeroTarjeta,
                                    onValueChange = { if (it.length <= 16) numeroTarjeta = it.filter { char -> char.isDigit() } },
                                    label = { Text("Número de tarjeta") },
                                    placeholder = { Text("1234 5678 9012 3456") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = nombreTitular,
                                    onValueChange = { nombreTitular = it },
                                    label = { Text("Nombre del titular") },
                                    placeholder = { Text("JUAN PEREZ") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = fechaVencimiento,
                                        onValueChange = { if (it.length <= 5) fechaVencimiento = it },
                                        label = { Text("MM/AA") },
                                        placeholder = { Text("12/25") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f)
                                    )

                                    OutlinedTextField(
                                        value = cvv,
                                        onValueChange = { if (it.length <= 3) cvv = it.filter { char -> char.isDigit() } },
                                        label = { Text("CVV") },
                                        placeholder = { Text("123") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                MetodoPago.YAPE, MetodoPago.PLIN -> {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Datos de pago",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = numeroCelular,
                                    onValueChange = { if (it.length <= 9) numeroCelular = it.filter { char -> char.isDigit() } },
                                    label = { Text("Número de celular") },
                                    placeholder = { Text("987654321") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    leadingIcon = {
                                        Text("+51", color = TextSecondary)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    color = InfoBlue.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Info,
                                            contentDescription = null,
                                            tint = InfoBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Recibirás un mensaje para confirmar el pago",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                MetodoPago.EFECTIVO -> {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarningYellow.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFF57C00),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Pago en agencia",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Deberás pagar en nuestras agencias antes de tu viaje. Se reservará tu asiento por 24 horas.",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                else -> {}
            }

            // Botón confirmar
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Button(
                    onClick = {
                        isProcessing = true
                        showDialogPago = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryOrange),
                    enabled = metodoPagoSeleccionado != null && !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Confirmar pago",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Diálogo de procesamiento de pago
    if (showDialogPago) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000) // Simular procesamiento
            isProcessing = false
            onPagoCompletado()
        }

        AlertDialog(
            onDismissRequest = { },
            confirmButton = { },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = PrimaryBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            text = {
                Text(
                    text = "Procesando tu pago...\nEspera un momento",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Composable
fun DetalleItem(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
        Text(
            text = valor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
    }
}

@Composable
fun MetodoPagoCard(
    titulo: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryBlueLight.copy(alpha = 0.2f) else Color.White
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, PrimaryBlue)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    tint = if (isSelected) PrimaryBlue else TextSecondary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) PrimaryBlue else TextPrimary
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = PrimaryBlue
                )
            }
        }
    }
}