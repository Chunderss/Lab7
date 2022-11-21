package com.example.test

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.ui.theme.TestTheme
import viewModel.AuthViewModel
import viewModel.UserLoginStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginPage()
                }
            }
        }
    }
}

@Composable
fun LoginPage(authViewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val localContext = LocalContext.current

    val loginStatus by authViewModel.userLoginStatus.collectAsState()

    var showFailedDialogue by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = loginStatus){
        when(loginStatus) {
            is UserLoginStatus.Failure -> {
                localContext.showToast("Login Unsuccessful")
                showFailedDialogue = true
            }
            UserLoginStatus.Successful -> {
                localContext.showToast("Login Successful")
            }
            null -> {

            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TheTextFields({ theEmail ->
            email = theEmail
        },
            { thePassword ->
                password = thePassword
            })
        Spacer(
            Modifier
                .height(10.dp)
        )
        LoginButton(
            onSignInClick = {
                when {
                    email.isBlank() -> {
                        localContext.showToast("Enter an email")
                    }

                    password.isBlank() -> {
                        localContext.showToast("Enter a password")
                    }

                    else -> {
                        authViewModel.performLogin(email,password)
                    }
                }
            },
        )
    }
}

@Composable
fun TheTextFields(
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit
) {

    var myEmail by remember { mutableStateOf("") }
    var myPassword by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = myEmail,
            onValueChange = {
                myEmail = it
                onEmailChanged(myEmail)
            },
            label = { Text(stringResource(R.string.email_text)) }
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        OutlinedTextField(
            value = myPassword,
            onValueChange = {
                myPassword = it
                onPasswordChanged(myPassword)
            },
            label = { Text(stringResource(R.string.password_text)) }
        )
    }
}

@Composable
fun LoginButton(
    onSignInClick: () -> Unit,
) {
    Button(onClick = {onSignInClick() }) {
        Text(stringResource(R.string.login_button))
    }
}

private fun Context.showToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}