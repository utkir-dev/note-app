package com.example.mynotes.presentation.utils.components

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
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.screens.main.pockets.PocketsViewModelImp
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun DialogPocket(
    vm: PocketsViewModelImp,
    listPocket: List<PocketDomain>,
    onDismiss: () -> Unit
) {

    //val context = LocalContext.current
    var pocketName by rememberSaveable {
        mutableStateOf(vm.getPocket().name)
    }

    var errorName by remember {
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
                    text = if (vm.getPocket().name.isEmpty()) "Yangi hamyon qo'shish" else
                        "${vm.getPocket().name} hamyon nomini o'zgartirish",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = pocketName,
                    onValueChange = { it ->
                        pocketName = it
                    },
                    label = {
                        MyText(
                            text = "Hamyon nomi",
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
                            var validPocket = true
                            val name = pocketName.trim()
                            val size =
                                listPocket.filter {
                                    it.name.lowercase() == name.lowercase()
                                }.size

                            if (name.isEmpty()) {
                                validPocket = false
                                errorName = "Hamyon nomini kiriting"
                            } else if (size > 0 && !vm.pocket.value.isValid()) {
                                validPocket = false
                                errorName = "Bu hamyon allaqachon bor"
                            } else {
                                errorName = ""
                            }
                            if (validPocket) {
                                vm.savePocket(name)
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


