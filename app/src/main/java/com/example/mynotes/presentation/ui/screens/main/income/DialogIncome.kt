package com.example.mynotes.presentation.ui.screens.main.income

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
fun DialogIncome(
    viewModel: IncomeViewModelImp, onDismiss: () -> Unit
) {
    val currencies by viewModel.currencies.collectAsStateWithLifecycle(emptyList())
    val balances by viewModel.balances.collectAsStateWithLifecycle(emptyList())

    val curency by viewModel.currency.collectAsStateWithLifecycle()
    val pocket by viewModel.pocket.collectAsStateWithLifecycle()


    var amountTransaction by rememberSaveable {
        mutableStateOf("")
    }
    var comment by rememberSaveable {
        mutableStateOf("")
    }

    var visibilityDropDownMenu by remember { mutableStateOf(false) }
    var visibilityDialogAttention by remember { mutableStateOf(false) }

    if (visibilityDialogAttention) {
        DialogAttention("") { visibilityDialogAttention = false }
    }
    val durationUp: Int = 250
    val durationDown: Int = 100
    val scaleUp: Float = 1.02f
    val scaleDown: Float = 0.8f
    val scale = remember {
        Animatable(1f)
    }
    LaunchedEffect(key1 = scale) {
        scale.animateTo(
            scaleDown,
            animationSpec = tween(durationDown),
        )
        scale.animateTo(
            scaleUp,
            animationSpec = tween(durationUp),
        )
        scale.animateTo(
            1f,
            animationSpec = tween(durationUp),
        )
    }
    Dialog(
        onDismissRequest = {
            onDismiss()
        }, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.scale(scale.value)
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
                            text = "${pocket.name} hamyonga qancha pul tushirmoqchisiz?",
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {

                            MyText(text = "Valyuta:", modifier = Modifier.padding(top = 4.dp))

                            ItemEmptyRow(horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalPading = 1.dp,
                                horizontalPading = 5.dp,
                                background = MaterialTheme.customColors.backgroundDialog,
                                onItemClicked = {
                                    visibilityDropDownMenu = !visibilityDropDownMenu
                                }) {
                                if (curency.name.isEmpty() && currencies.isNotEmpty()) {
                                    viewModel.setCurrency(currencies[0])
                                }
                                MyText(
                                    text = if (curency.name.isNotEmpty()) curency.name else if (currencies.isNotEmpty()) currencies[0].name else "Dollar",
                                    color = MaterialTheme.customColors.textColor,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_spinner),
                                    contentDescription = "spinner"
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 200.dp)
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
                                    val (isValid, amount) = isValidAmount(amountTransaction)
                                    if (isValid) {
                                        if (curency.id.isEmpty()) {
                                            viewModel.setCurrency(currencies[0])
                                        }
                                        viewModel.addTransaction(
                                            amount,
                                            comment,
                                            balances.sumOf { it.amount * (1 / it.rate) }
                                        )
                                        onDismiss()
                                    }
                                }, text = "Tasdiq",
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green,
                                    contentColor = White
                                ), modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) {}
                        }
                    }
                }

                if (visibilityDropDownMenu) LazyColumn(
                    modifier = Modifier
                        .padding(top = 32.dp, end = 16.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.customColors.borderColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .heightIn(max = 300.dp)
                        .width(180.dp)
                        .background(MaterialTheme.customColors.backgroundDialog)
                        .padding(6.dp)
                ) {
                    items(items = currencies, key = { currencyDomain ->
                        currencyDomain.hashCode()
                    }) { currencyDomain ->
                        Text(text = currencyDomain.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setCurrency(currencyDomain)
                                    visibilityDropDownMenu = !visibilityDropDownMenu
                                }
                                .padding(vertical = 5.dp),
                            color = MaterialTheme.customColors.textColor)
                    }
                }
            }
        }
    }
}

private fun isValidAmount(amount: String): Pair<Boolean, Double> {
    var n = -1.0
    try {
        n = amount.trim().toDouble()
    } catch (_: Exception) {
    }

    return Pair(n > 0, n)
}

