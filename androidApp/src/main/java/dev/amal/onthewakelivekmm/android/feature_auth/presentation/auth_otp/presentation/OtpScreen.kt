package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardTextField
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.model.OtpResult
import dev.amal.onthewakelivekmm.android.navigation.Screen
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp.OtpEvent
import dev.amal.onthewakelivekmm.feature_auth.presentation.auth_otp.OtpEvent.OtpChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun OtpScreen(
    navController: NavHostController,
    viewModel: AndroidOtpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val androidOtpState = viewModel.androidOtpState

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        viewModel.otpResult.collectLatest { result ->
            when (result) {
                OtpResult.IncorrectOtp -> snackBarHostState.showSnackbar(
                    message = "Incorrect OTP"
                )
                OtpResult.OtpTooManyRequests -> snackBarHostState.showSnackbar(
                    message = "OtpTooManyRequests"
                )
                else -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.unknown_error)
                )
            }
        }
    }

    LaunchedEffect(key1 = state.signUpResult) {
        state.signUpResult?.let { signUpResult ->
            when (signUpResult) {
                AuthResult.Authorized -> navController.navigate(Screen.QueueScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
                else -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.unknown_error)
                )
            }
            viewModel.onEvent(OtpEvent.OnSignUpResultSeen)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_back_icon)
                        )
                    }
                }
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.verify_phone_number),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "An 6 digit code has been sent to\n${androidOtpState.signUpPhoneNumber}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.otp,
                    onValueChange = {
                        if (it.length <= 6) viewModel.onEvent(OtpChanged(it))
                        if (it.length == 6) {
                            focusManager.clearFocus()
                            viewModel.verifyOtpAndSignUp()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    label = "Code",
                    errorText = state.otpError
                )
                Spacer(modifier = Modifier.height(4.dp))
                Timer(onResendClicked = { viewModel.resendCode(context) })
            }
        }
    }
//    if (state.isLoading) StandardLoadingView()
}

@Composable
fun Timer(onResendClicked: () -> Unit) {

    var currentTime by remember { mutableStateOf(60L) }

    LaunchedEffect(key1 = currentTime) {
        if (currentTime > 0) {
            delay(1000L)
            currentTime -= 1L
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Didn't receive code? Resend after ",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${currentTime}s",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        TextButton(onClick = onResendClicked, enabled = currentTime <= 0) {
            Text(text = "Resend")
        }
    }
}