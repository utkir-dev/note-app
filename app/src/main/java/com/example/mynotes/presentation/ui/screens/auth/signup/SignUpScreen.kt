package com.example.mynotes.presentation.ui.screens.auth.signup


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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.presentation.ui.directions.common.UiState
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.dialogs.DialogBoxLoading
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.connection.NetworkState
import com.example.mynotes.presentation.utils.connection.networkState

class SignUpScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SignUpViewModelImp = getViewModel()
        val state = viewModel.uiState.collectAsState()
        SignUp(viewModel, state)
    }
}

@Composable
fun SignUp(
    vm: SignUpViewModelImp,
    state: State<UiState>

) {
    val context = LocalContext.current
    val success by vm.success.collectAsStateWithLifecycle()
    var login by rememberSaveable { mutableStateOf("") }
    var password1 by rememberSaveable { mutableStateOf("") }
    var password2 by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var visibilityAlert by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    if (visibilityAlert) {
        DialogAttention(message = message) {
            vm.changeUiState(UiState.Default)
            visibilityAlert = false
        }
    }
    if (success) {
        DialogAttention(
            time = 30,
            message = "Muvaffaqiyatli ro'yxatdan o'tdingiz.\nlogin: $login\nparol: $password1\nbularni esdan chiqarmang!"
        ) {
            vm.changeUiState(UiState.Default)
            vm.back()
        }
    }
    val icon = if (passwordVisibility) {
        painterResource(id = com.example.mynotes.R.drawable.baseline_visibility_24)
    } else {
        painterResource(id = com.example.mynotes.R.drawable.baseline_visibility_off_24)

    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            MyText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "Ro'yxatdan o'tish",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.customColors.textColor
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.customColors.backgroundBrush)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 15.dp),
                value = login,
                onValueChange = { it ->
                    login = it
                },
                label = {
                    MyText(
                        text = "Login yoki email",
                        color = MaterialTheme.customColors.subTextColor
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(15.dp),
                colors = buttonColors()
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 15.dp),
                value = password1,
                onValueChange = { it ->
                    password1 = it
                },
                label = {
                    MyText(
                        text = "Parol",
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onDone),
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                colors = buttonColors()
            )

            OutlinedTextField(
                value = password2,
                onValueChange = { it ->
                    password2 = it
                },
                label = {
                    MyText(
                        text = "Parolni takrorlang",
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
                    keyboardType = KeyboardType.Password,
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
                    if (context.networkState is NetworkState.AVAILABLE) {
                        if (login.trim().isEmpty()) {
                            message = "Login kiritilmadi"
                            visibilityAlert = true
                        } else if (password1.trim().isEmpty()) {
                            message = "Parol kiritilmadi"
                            visibilityAlert = true
                        } else if (password2.trim().isEmpty()) {
                            message = "Parol qayta kiritilmadi"
                            visibilityAlert = true
                        } else if (password1 != password2) {
                            message = "Parollar mos kelmadi"
                            visibilityAlert = true
                        } else {
                            vm.signUp(login.trim(), password1.trim())
                        }
                    } else {
                        message = "Internet yo'qilmagan"
                        visibilityAlert = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = White
                )
            ) {
                MyText(text = "Registraciya", fontSize = 18.sp, color = Color.White)
            }
        }
    }
    when (state.value) {
        is UiState.Progress -> {
            DialogBoxLoading()
        }
        is UiState.Error -> {
            message = "Xatolik. Bu email ro'yxatdan o'tgan bo'lishi mumkin"
            visibilityAlert = true
        }

    }
}
