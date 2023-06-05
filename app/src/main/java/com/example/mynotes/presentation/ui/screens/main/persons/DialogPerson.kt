package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun DialogPerson(
    vm: PersonsViewModelImp,
    listPerson: List<PersonDomain>,
    onDismiss: () -> Unit

) {
    var name by rememberSaveable {
        mutableStateOf(vm.getPerson().name)
    }
    var phone by rememberSaveable {
        mutableStateOf(vm.getPerson().phone)
    }
    var address by rememberSaveable {
        mutableStateOf(vm.getPerson().address)
    }
    var errorName by rememberSaveable {
        mutableStateOf("")
    }
    var errorPhone by rememberSaveable {
        mutableStateOf("")
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
                .scale(scale.value)
                .padding(8.dp)

        ) {
            Column(
                Modifier
                    .background(MaterialTheme.customColors.backgroundDialog)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyText(
                    text = if (vm.getPerson().name.isEmpty()) "Yangi odam qo'shish" else
                        "${vm.getPerson().name}ni o'zgartirish",
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
                    text = errorName,
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
                MyText(
                    text = errorPhone,
                    fontSize = 12.sp,
                    color = MaterialTheme.customColors.errorText
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
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(15.dp),
                    colors = buttonColors()
                )



                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        onClick = { onDismiss() },
                        text = "Bekor",
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gray,
                            contentColor = White
                        ),
                        textSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) { }

                    MyButton(
                        onClick = {

                            var validPerson = true
                            var validPhone = true

                            val namePerson = name.trim()

                            val personFound =
                                listPerson.firstOrNull {
                                    it.phone.isNotEmpty() && it.phone.trim()
                                        .lowercase() == phone.trim().lowercase()
                                }

                            if (namePerson.isEmpty()) {
                                validPerson = false
                                errorName = "Ismini kiriting"
                            } else {
                                errorName = ""
                            }
                            if (personFound != null && !vm.person.value.isValid()) {
                                validPhone = false
                                errorPhone = "Bu ${personFound.name}ning raqami"
                            } else {
                                errorPhone = ""
                            }

                            if (validPerson && validPhone) {
                                vm.savePerson(name.trim(), phone.trim(), address.trim())
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


