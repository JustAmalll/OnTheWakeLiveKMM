package dev.amal.onthewakelivekmm.android.feature_queue.presentation.queue.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardTextField
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

@ExperimentalMaterial3Api
@Composable
fun AdminDialog(
    showDialog: (Boolean) -> Unit,
    onAddClicked: (Boolean, String) -> Unit,
    queue: List<QueueItem>
) {
    val context = LocalContext.current

    var isLeftButtonActive by remember { mutableStateOf(false) }
    var isRightButtonActive by remember { mutableStateOf(true) }

    var firstNameFieldState by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }

    val rightButtonColor = if (isRightButtonActive) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onPrimary
    val leftButtonColor = if (isLeftButtonActive) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onPrimary

    Dialog(onDismissRequest = { showDialog(false) }) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.add_to_queue),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            isLeftButtonActive = !isLeftButtonActive
                            isRightButtonActive = !isRightButtonActive
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = leftButtonColor,
                            contentColor = rightButtonColor
                        )
                    ) {
                        Text(text = stringResource(id = R.string.left_line_admin))
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = {
                            isLeftButtonActive = !isLeftButtonActive
                            isRightButtonActive = !isRightButtonActive
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = rightButtonColor,
                            contentColor = leftButtonColor
                        )
                    ) {
                        Text(text = stringResource(id = R.string.right_line_admin))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                StandardTextField(
                    value = firstNameFieldState,
                    onValueChange = { firstNameFieldState = it },
                    label = stringResource(id = R.string.first_name),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    errorText = errorMessage
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val addToQueueResult = ValidationUseCase(context = context)
                            .validateAdminAddToQueue(
                                firstName = firstNameFieldState, queue = queue
                            )
                        errorMessage = addToQueueResult.errorMessage

                        if (addToQueueResult.successful) {
                            onAddClicked(isLeftButtonActive, firstNameFieldState)
                            showDialog(false)
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.add))
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}