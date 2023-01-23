package dev.amal.onthewakelivekmm.android.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.amal.onthewakelivekmm.android.R

@Composable
fun FormattedDateOfBirth(dateOfBirth: String) {
    Column {
        val formattedDateOfBirth = if (dateOfBirth.isNotEmpty()) "${dateOfBirth.take(2)}/${
            dateOfBirth.drop(2).dropLast(4)
        }/${dateOfBirth.takeLast(4)}" else stringResource(id = R.string.not_specified)

        Text(
            text = stringResource(id = R.string.date_of_birth),
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = formattedDateOfBirth)
    }
}