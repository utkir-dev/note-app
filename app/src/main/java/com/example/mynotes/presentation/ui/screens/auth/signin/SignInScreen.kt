package com.example.mynotes.presentation.ui.screens.auth.signin


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.example.mynotes.R
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

class SignInScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SignInViewModelImp = getViewModel()
        val state = viewModel.uiState.collectAsStateWithLifecycle()

        ShowScreen(state, viewModel)
    }
}

@Composable
fun ShowScreen(
    state: State<UiState>,
    vm: SignInViewModelImp
) {
    val context = LocalContext.current
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var visibilityAlert by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    if (visibilityAlert) {
        DialogAttention(message = message) {
            vm.changeUiState(UiState.Default)
            visibilityAlert = false
        }
    }
    val icon = if (passwordVisibility) {
        painterResource(id = R.drawable.baseline_visibility_24)
    } else {
        painterResource(id = R.drawable.baseline_visibility_off_24)

    }
    val scrollState = rememberScrollState()
//    Image(
//        modifier = Modifier.fillMaxSize(),
//        painter = painterResource(id = R.drawable.splash_background),
//        contentDescription = "splash",
//        contentScale = ContentScale.Crop,
//        alpha = 0.5f
//    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyText(
            modifier = Modifier
                .padding(24.dp),
            text = "Kirish",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
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
            value = password,
            onValueChange = { it ->
                password = it
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
                    } else if (password.trim().isEmpty()) {
                        message = "Parol kiritilmadi"
                        visibilityAlert = true
                    } else if (password.trim().length < 6) {
                        message = "Parol uzunligi kamida 6 ta bo'lishi kerak!"
                        visibilityAlert = true
                    } else {
                        vm.signIn(login.trim(), password.trim())
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
            MyText(text = "Tasdiqlash", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(25.dp))
        MyText(
            modifier = Modifier.clickable {
                vm.signUp()
            },
            text = "Agar avval ro'yxatdan o'tmagan bo'lsangiz bu yerni bosing",
            textAlign = TextAlign.Center
        )


    }
    when (state.value) {
        is UiState.Progress -> {
            DialogBoxLoading()
        }
        is UiState.Error -> {
            message = "Login yoki parol noto'g'ri kiritildi"
            visibilityAlert = true
        }
    }
}

