package com.example.mynotes.presentation.ui.screens.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.contstants.DEFAULT_PINCODE

class SettingsScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SettingsViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(
    vm: SettingsViewModelImp,

    ) {
    val pincode by vm.pincode.collectAsStateWithLifecycle()
    val success by vm.success.collectAsStateWithLifecycle()
    var password by rememberSaveable { mutableStateOf("") }
    var password1 by rememberSaveable { mutableStateOf("") }
    var password2 by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var visibilityAlert by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    if (visibilityAlert) {
        DialogAttention(message = message) {
            visibilityAlert = false
        }
    }
    if (success) {
        DialogAttention(
            time = 20,
            message = "Pinkod o'zgartirildi.\npinkod: $password1\nbuni esdan chiqarmang!"
        ) {
            vm.savePickode(password1.trim())
            vm.back()
        }
    }
    val icon = if (passwordVisibility) {
        painterResource(id = R.drawable.baseline_visibility_24)
    } else {
        painterResource(id = R.drawable.baseline_visibility_off_24)

    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    vm.back()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back arrow",
                        tint = MaterialTheme.customColors.textColor
                    )
                }
                MyText(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(start = 10.dp),
                    text = "Sozlashlar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.customColors.backgroundBrush)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyText(
                text = "Ekran pinkodini o'zgartirish",
                color = MaterialTheme.customColors.textColor,
                fontSize = 18.sp
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                value = password,
                onValueChange = { it ->
                    password = it
                },
                label = {
                    MyText(
                        text = "Hozirgi pinkod",
                        color = MaterialTheme.customColors.subTextColor
                    )
                }, trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(painter = icon, contentDescription = "Icon Visibility")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onDone),
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                colors = buttonColors()
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                value = password1,
                onValueChange = { it ->
                    password1 = it
                },
                label = {
                    MyText(
                        text = "Yangi pinkod",
                        color = MaterialTheme.customColors.subTextColor
                    )
                }, trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(painter = icon, contentDescription = "Icon Visibility")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onDone),
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                colors = buttonColors()
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password2,
                onValueChange = { it ->
                    password2 = it
                },
                label = {
                    MyText(
                        text = "Yangini takrorlang",
                        color = MaterialTheme.customColors.subTextColor
                    )
                }, trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(painter = icon, contentDescription = "Icon Visibility")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onDone),
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                colors = buttonColors()
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    val pin = if ((pincode ?: "").isEmpty()) DEFAULT_PINCODE else pincode
                    if (password.trim().isEmpty()) {
                        message = "Hozirgi pinkod kiritilmadi"
                        visibilityAlert = true
                    } else if (pin != password.trim()) {
                        message = "Pinkod noto'g'ri kiritildi"
                        visibilityAlert = true
                    } else if (password1.trim().isEmpty()) {
                        message = "Yangi pinkod kiritilmadi"
                        visibilityAlert = true
                    } else if (password2.trim().isEmpty()) {
                        message = "Yangi pinkod qayta kiritilmadi"
                        visibilityAlert = true
                    } else if (password1 != password2) {
                        message = "Pinkodlar mos kelmadi"
                        visibilityAlert = true
                    } else {
                        vm.success.value = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = White
                )
            ) {
                MyText(
                    text = "O'zgartirish",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}
