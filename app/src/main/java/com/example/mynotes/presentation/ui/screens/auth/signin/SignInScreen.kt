package com.example.mynotes.presentation.ui.screens.auth.signin


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.presentation.ui.directions.common.UiState
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.progress.MyCircularProgressBar
import com.example.mynotes.presentation.utils.components.text.MyText

class SignInScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SignInViewModelImp = getViewModel()
        val state = viewModel.uiState.collectAsState()
        val signIn = viewModel::signIn
        val signUp = viewModel::signUp
        SignIn(state, signIn, signUp)
    }
}

@Composable
fun SignIn(
    state: State<UiState>,
    signIn: (login: String, password: String) -> Unit,
    signUp: () -> Unit
) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val icon = if (passwordVisibility) {
        painterResource(id = com.example.mynotes.R.drawable.baseline_visibility_24)
    } else {
        painterResource(id = com.example.mynotes.R.drawable.baseline_visibility_off_24)

    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
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
                    text = "Login",
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
                signIn(login, password)

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
                signUp()
            },
            text = "Agar avval ro'yxatdan o'tmagan bo'lsangiz bu yerni bosing",
            textAlign = TextAlign.Center
        )


    }
    when (state.value) {
        is UiState.Progress -> {
            MyCircularProgressBar()
        }
        is UiState.Error -> {

        }
    }
}

