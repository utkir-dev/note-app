package com.example.mynotes.presentation.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.image.*
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun DialogCurrency(onDismiss: (String, Double, Boolean) -> Unit) {
    //val context = LocalContext.current
    var textValue by rememberSaveable {
        mutableStateOf("")
    }
    var textValidation by rememberSaveable {
        mutableStateOf(false)
    }
    var rateValue by rememberSaveable {
        mutableStateOf("")
    }
    var rateValidation by rememberSaveable {
        mutableStateOf(false)
    }
    Dialog(
        onDismissRequest = {
            onDismiss(textValue, rateValue.toDouble(), false)
        }, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.customColors.backgroundDialog)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyText(
                    text = "Yangi valyuta qo'shish",
                    modifier = Modifier.padding(8.dp),
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
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.customColors.textColor,
                        focusedBorderColor = MaterialTheme.customColors.subTextColor,
                        focusedLabelColor = MaterialTheme.customColors.subTextColor,
                        unfocusedBorderColor = MaterialTheme.customColors.subTextColor,
                        cursorColor = MaterialTheme.customColors.textColor
                    )
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.customColors.textColor,
                            focusedBorderColor = MaterialTheme.customColors.subTextColor,
                            focusedLabelColor = MaterialTheme.customColors.subTextColor,
                            unfocusedBorderColor = MaterialTheme.customColors.subTextColor,
                            cursorColor = MaterialTheme.customColors.textColor
                        )
                    )
                }
                MyText(
                    text = if (rateValidation) "Kursni to'g'ri kiriting" else "",
                    fontSize = 14.sp,
                    color = MaterialTheme.customColors.errorText
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        onClick = { onDismiss("", 0.0, false) },
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
                                onDismiss(textValue, rateValue.toDouble(), true)
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
