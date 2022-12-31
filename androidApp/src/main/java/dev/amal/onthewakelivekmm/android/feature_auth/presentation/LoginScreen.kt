package dev.amal.onthewakelivekmm.android.feature_auth.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardTextField
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login.LoginEvent
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_login.LoginState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val haptic = LocalHapticFeedback.current
    val systemBarsColor = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = systemBarsColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = state.loginResult) {
        state.loginResult?.let { loginResult ->
            when (loginResult) {
                is AuthResult.Authorized -> snackBarHostState.showSnackbar(
                    message = "Success"
                )
                is AuthResult.IncorrectData -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.incorrect_data)
                )
                else -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.unknown_error)
                )
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signInPhoneNumber,
                    onValueChange = {
                        onEvent(LoginEvent.SignInPhoneNumberChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    label = stringResource(id = R.string.phone_number),
                    isError = state.signInPhoneNumberError != null,
                    errorText = state.signInPhoneNumberError,
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signInPassword,
                    onValueChange = {
                        onEvent(LoginEvent.SignInPasswordChanged(it))
                    },
                    label = stringResource(id = R.string.password),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onEvent(LoginEvent.SignIn)
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            focusManager.clearFocus()
                        }
                    ),
                    isError = state.signInPasswordError != null,
                    errorText = state.signInPasswordError,
                    isPasswordTextField = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onEvent(LoginEvent.SignIn)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 12.dp)
                    .clickable {
//                        navController.navigate(Screen.RegisterScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.dont_have_an_account_yet),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${stringResource(id = R.string.sign_up)}!",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
//    if (state.isLoading) StandardLoadingView()
}