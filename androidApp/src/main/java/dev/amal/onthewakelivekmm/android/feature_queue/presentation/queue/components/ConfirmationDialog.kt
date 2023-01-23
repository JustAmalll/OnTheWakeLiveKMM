package dev.amal.onthewakelivekmm.android.feature_queue.presentation.queue.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.amal.onthewakelivekmm.android.R

@Composable
fun ConfirmationDialog(
    showDialog: (Boolean) -> Unit,
    isUserAdmin: Boolean,
    onLeaveQueue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showDialog(false) },
        title = {
            Text(text = stringResource(R.string.confirm_action))
        },
        text = {
            Text(
                text = if (isUserAdmin) stringResource(R.string.admin_remove_person_confirmation_text)
                else stringResource(R.string.leave_queue_confirmation_text)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLeaveQueue()
                    showDialog(false)
                }
            ) {
                Text(
                    text = if (isUserAdmin) stringResource(R.string.remove)
                    else stringResource(R.string.leave)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { showDialog(false) }) {
                Text(
                    text = if (isUserAdmin) stringResource(R.string.cancel)
                    else stringResource(R.string.stay)
                )
            }
        }
    )
}