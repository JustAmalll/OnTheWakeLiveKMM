package dev.amal.onthewakelivekmm.android.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.amal.onthewakelivekmm.android.R

@ExperimentalMaterial3Api
@Composable
fun StandardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(onDone = {}),
    isPasswordTextField: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = errorText != null,
        visualTransformation = if (!showPassword && isPasswordTextField)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPasswordTextField && value.isNotEmpty()) {
                val image =
                    if (showPassword) painterResource(id = R.drawable.ic_visibility_off)
                    else painterResource(id = R.drawable.ic_visibility_on)

                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(painter = image, contentDescription = "Toggle password visibility icon")
                }
            }
        }
    )
    if (errorText != null) Text(
        modifier = Modifier.fillMaxWidth(),
        text = errorText,
        color = MaterialTheme.colorScheme.error,
        fontSize = 14.sp,
        textAlign = TextAlign.End
    )
}