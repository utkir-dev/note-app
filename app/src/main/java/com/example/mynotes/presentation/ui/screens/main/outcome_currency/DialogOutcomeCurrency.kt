package com.example.mynotes.presentation.ui.screens.main.outcome_pocket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.screens.main.outcome_currency.OutcomeCurrencyViewModel
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemEmptyRow

@Composable
fun DialogOutcomeCurrency(
    viewModel: OutcomeCurrencyViewModel, onDismiss: () -> Unit
) {
    val pocket by remember { viewModel.pocket }
    val balances by viewModel.balances.collectAsStateWithLifecycle(emptyList())

    val wallet by remember { viewModel.wallet }
    var attentionText by remember { mutableStateOf("") }

    val curency by remember {
        viewModel.currency
    }

    var amountTransaction by rememberSaveable {
        mutableStateOf("")
    }
    var comment by rememberSaveable {
        mutableStateOf("")
    }

    var visibilityDialogAttention by remember { mutableStateOf(false) }

    var visibilityValidation by remember { mutableStateOf(false) }

    if (visibilityDialogAttention) {
        DialogAttention(attentionText) { visibilityDialogAttention = false }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        }, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                LazyColumn(
                    Modifier
                        .background(MaterialTheme.customColors.backgroundDialog)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        MyText(
                            text = "${pocket.name} hamyonda\n${wallet.balance.huminize()} ${curency.name} bor. Qancha chiqim qilmoqchisiz?",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.customColors.textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp), value = amountTransaction,
                            onValueChange = {
                                amountTransaction = it
                            },
                            label = {
                                MyText(
                                    text = "Summa kiriting",
                                    color = MaterialTheme.customColors.subTextColor
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = buttonColors()
                        )
                    }
                    item {
                        MyText(
                            text = if (visibilityValidation) "Summa noto'g'ri kiritildi" else "",
                            fontSize = 14.sp,
                            color = MaterialTheme.customColors.errorText,
                        )
                    }
                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 160.dp)
                                .padding(5.dp),
                            value = comment,
                            onValueChange = {
                                comment = it
                            },
                            label = {
                                MyText(
                                    text = "Izoh (ixtiyoriy)",
                                    color = MaterialTheme.customColors.subTextColor
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = buttonColors()
                        )
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MyButton(
                                onClick = { onDismiss() },
                                text = "Bekor",
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Gray, contentColor = White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) { }

                            MyButton(
                                onClick = {
                                    val (isValid, amount) = isValidAmount(
                                        amountTransaction,
                                        wallet.balance
                                    )
                                    if (isValid) {
                                        viewModel.addTransaction(
                                            amount,
                                            comment,
                                            balances.sumOf { it.amount * (1 / it.rate) }
                                        )
                                        visibilityValidation = false
                                        attentionText =
                                            "${pocket.name} dan ${amount.huminize()} ${curency.name} chiqim qilindi"
                                        visibilityDialogAttention = true
                                        onDismiss()
                                    } else {
                                        visibilityValidation = true
                                    }
                                }, text = "Tasdiq", colors = ButtonDefaults.buttonColors(
                                    containerColor = Green, contentColor = White
                                ), modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

private fun isValidAmount(amount: String, balance: Double): Pair<Boolean, Double> {
    var n = -1.0
    try {
        n = amount.trim().toDouble()
    } catch (_: Exception) {
    }
    return Pair(0 < n && n <= balance, n)
}

