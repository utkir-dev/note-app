package com.example.mynotes.presentation.utils.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.presentation.ui.screens.main.currencies.CurrencyViewModelImp
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun DialogCurrency(
    vm: CurrencyViewModelImp,
    listCurrency: List<CurrencyDomain>,
    onDismiss: () -> Unit
) {

    //val context = LocalContext.current
    var currencyName by rememberSaveable {
        mutableStateOf(vm.getCurrency().name)
    }
    var errorName by remember {
        mutableStateOf("")
    }
    var errorRate by remember {
        mutableStateOf("")
    }
    var currencyRate by rememberSaveable {
        mutableStateOf(if (vm.getCurrency().rate > 0) vm.getCurrency().rate.toString() else "")
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
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .scale(
                    scale = scale.value
                )
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
                    text = if (vm.getCurrency().name.isEmpty()) "Yangi valyuta qo'shish"
                    else "${vm.getCurrency().name} valyutani o'zgartirish",
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
                    value = currencyName,
                    onValueChange = { it ->
                        currencyName = it
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
                    text = errorName,
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
                        value = currencyRate,
                        onValueChange = {
                            currencyRate = it
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
                    text = errorRate,
                    fontSize = 14.sp,
                    color = MaterialTheme.customColors.errorText
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        onClick = { onDismiss() },
                        text = "Bekor",
                        textSize = 16.sp,
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
                            var validCurrency = true
                            val name = currencyName.trim().lowercase()
                            val rate = currencyRate.trim().lowercase()
                            val size =
                                listCurrency.filter {
                                    it.name.lowercase() == name
                                }.size

                            if (name.isEmpty()) {
                                validCurrency = false
                                errorName = "Valyuta nomini kiriting"
                            } else if (size > 0 && !vm.currency.value.isValid()) {
                                validCurrency = false
                                errorName = "Bu valyuta allaqachon bor"
                            } else {
                                errorName = ""
                            }

                            var validRate = true
                            var number = -1.0
                            try {
                                number = rate.trim().toDouble()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            if (number < 0) {
                                validRate = false
                            }

                            if (!validRate) {
                                errorRate = "Kursni to'g'ri kiriting"
                            } else {
                                errorRate = ""
                            }
                            if (validCurrency && validRate) {
                                vm.saveCurrency(name, rate)
                                onDismiss()
                            }
                        },
                        text = "Tasdiq",
                        textSize = 16.sp,
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

