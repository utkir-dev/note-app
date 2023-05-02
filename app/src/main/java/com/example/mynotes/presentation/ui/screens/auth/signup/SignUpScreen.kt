package com.example.mynotes.presentation.ui.screens.auth.signup


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.presentation.ui.directions.common.UiState
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.progress.MyCircularProgressBar
import com.example.mynotes.presentation.utils.components.text.MyText

class SignUpScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SignUpViewModelImp = getViewModel()
        val dispatcher = viewModel::signUp
        val state = viewModel.uiState.collectAsState()
        SignUp(dispatcher,state)
    }

}

@Composable
fun SignUp(
    dispatcher: (login: String, password: String) -> Unit,
    state: State<UiState>

) {
    var login by rememberSaveable { mutableStateOf("") }
    var password1 by rememberSaveable { mutableStateOf("") }
    var password2 by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val icon = if (passwordVisibility) {
        painterResource(id = com.example.mynotes.R.drawable.baseline_visibility_24)
    } else {
        painterResource(id = com.example.mynotes.R.drawable.baseline_visibility_off_24)

    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { MyText(text = "Sign Up") }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(bottom = 15.dp),
                value = login,
                onValueChange = { it ->
                    login = it
                },
                //  placeholder = { Text(text = "Login") },
                label = { MyText(text = "Login") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = password1,
                onValueChange = { it ->
                    password1 = it
                },
                label = { MyText(text = "Parol") },
                trailingIcon = {
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
                else PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = password2,
                onValueChange = { it ->
                    password2 = it
                },
                label = { MyText(text = "Parol") },
                trailingIcon = {
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
                else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(onClick = {
                if (password1==password2){
                    dispatcher(login, password1)
                }else{
                 // Toast.makeText()
                }


            }) {
                MyText(text = "Registraciya", fontSize = 18.sp)
            }
        }
    }
    when (state.value) {
        is UiState.Progress -> {
            MyCircularProgressBar()
        }
        is UiState.Error -> {

        }
    }

}
