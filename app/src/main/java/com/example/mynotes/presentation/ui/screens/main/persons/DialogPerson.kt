package com.example.mynotes.presentation.ui.screens.main.persons

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
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.image.*
import com.example.mynotes.presentation.utils.components.text.MyText
import java.util.*

@Composable
fun DialogPerson(person: PersonDomain, onDismiss: (PersonDomain?) -> Unit) {
    var name by rememberSaveable {
        mutableStateOf(person.name)
    }
    var phone by rememberSaveable {
        mutableStateOf(person.phone)
    }
    var address by rememberSaveable {
        mutableStateOf(person.address)
    }
    var nameValidation by rememberSaveable {
        mutableStateOf(true)
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
                    text = if (person.name.isEmpty()) "Yangi odam qo'shish" else
                        "${person.name}ni o'zgartirish",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 1.dp),
                    value = name,
                    onValueChange = { it ->
                        name = it
                    },
                    label = {
                        MyText(
                            text = "Ismi",
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
                    text = if (!nameValidation) "Ismini kiriting" else "",
                    fontSize = 12.sp,
                    color = MaterialTheme.customColors.errorText
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 1.dp),
                    value = phone,
                    onValueChange = { it ->
                        phone = it
                    },
                    label = {
                        MyText(
                            text = "Tel raqam (ixtiyoriy)",
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
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 1.dp),
                    value = address,
                    onValueChange = { it ->
                        address = it
                    },
                    label = {
                        MyText(
                            text = "Manzil (ixtiyoriy)",
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
                            nameValidation = validateName(name)
                            if (nameValidation) {
                                val personNew = if (person.isValid()) {
                                    person.copy(
                                        name = name,
                                        phone = phone,
                                        address = address,
                                        date = System.currentTimeMillis()
                                    )
                                } else PersonDomain(
                                    id = UUID.randomUUID().toString(),
                                    name = name,
                                    phone = phone,
                                    address = address,
                                    date = System.currentTimeMillis()
                                )
                                onDismiss(personNew)
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

private fun validateName(textValue: String) = textValue.trim().isNotEmpty()


