package com.example.mynotes.presentation.utils.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.image.*
import com.example.mynotes.presentation.utils.components.text.MyText
import java.util.*

@Composable
fun DialogCurrency(currency: CurrencyDomain, onDismiss: (CurrencyDomain?) -> Unit) {

    //val context = LocalContext.current
    var textValue by rememberSaveable {
        mutableStateOf(currency.name)
    }
    var textValidation by rememberSaveable {
        mutableStateOf(false)
    }
    var rateValue by rememberSaveable {
        mutableStateOf(currency.rate.toString())
    }
    var rateValidation by rememberSaveable {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = {
            onDismiss(null)
        }, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(8.dp)

        ) {
            Column(
                Modifier
                    .background(MaterialTheme.customColors.backgroundDialog)
                    .padding(16.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyText(
                    text = if (currency.name.isEmpty()) "Yangi valyuta qo'shish"
                    else "${currency.name} valyutani o'zgartirish",
                    modifier = Modifier
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = textValue,
                    onValueChange = { it ->
                        textValue = it
                    },
                    label = {
                        MyText(
                            text = "Valyuta nomi",
                            color = MaterialTheme.customColors.subTextColor
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(15.dp),
                    colors = buttonColors()
                )
                MyText(
                    text = if (textValidation) "Valyuta nomini kiriting" else "",
                    fontSize = 14.sp,
                    color = MaterialTheme.customColors.errorText
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(
                        text = "kurs 1$ = ",
                        fontSize = 16.sp,
                        color = MaterialTheme.customColors.subTextColor
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(5.dp),
                        value = rateValue,
                        onValueChange = {
                            rateValue = it
                        },
                        placeholder = {
                            MyText(
                                text = "0.0",
                                color = MaterialTheme.customColors.subTextColor
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = buttonColors()
                    )
                }
                MyText(
                    text = if (rateValidation) "Kursni to'g'ri kiriting" else "",
                    fontSize = 14.sp,
                    color = MaterialTheme.customColors.errorText
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        onClick = { onDismiss(null) },
                        text = "Bekor",
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gray,
                            contentColor = White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) { }

                    MyButton(
                        onClick = {
                            textValidation = validateName(textValue)
                            rateValidation = validateRate(rateValue)
                            if (!textValidation && !rateValidation) {
                                val currencyNew = if (currency.isValid()) {
                                    currency.copy(
                                        name = textValue,
                                        rate = rateValue.toDouble()
                                    )
                                } else CurrencyDomain(
                                    id = UUID.randomUUID().toString(),
                                    name = textValue,
                                    rate = rateValue.toDouble(),
                                    date = System.currentTimeMillis()
                                )
                                onDismiss(currencyNew)
                            }
                        },
                        text = "Tasdiq",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green,
                            contentColor = White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)

                    ) {
                    }
                }

            }

        }
    }
}

private fun validateName(textValue: String) = textValue.trim().isEmpty()

private fun validateRate(rateValue: String): Boolean {
    var number = -1.0
    try {
        number = rateValue.trim().toDouble()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return number <= 0
}
