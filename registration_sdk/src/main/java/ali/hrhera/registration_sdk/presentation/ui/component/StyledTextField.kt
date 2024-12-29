package ali.hrhera.registration_sdk.presentation.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    label: String,
    errorMessage: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String?) -> Unit
) {
    if (keyboardType == KeyboardType.Password || keyboardType == KeyboardType.NumberPassword) {
        StyledPasswordField(modifier, label = label, errorMessage = errorMessage, keyboardType = keyboardType, onValueChange)
    } else {
        val value = remember { mutableStateOf("") }
        val shake = remember { Animatable(0f) }
        LaunchedEffect(key1 = errorMessage) {
            if (errorMessage.isNullOrBlank().not()) {
                for (i in 0..6) {
                    when (i % 2) {
                        0 -> shake.animateTo(5f, spring(stiffness = 100_00f))
                        else -> shake.animateTo(-5f, spring(stiffness = 100_00f))
                    }
                }
                shake.animateTo(0f)
            }
        }

        TextField(
            modifier = if (errorMessage.isNullOrBlank().not()) modifier.offset {
                IntOffset(
                    x = (shake.value * 10).roundToInt(), y = 0
                )
            }
            else modifier,
            value = value.value,
            onValueChange = { new: String ->
                value.value = new
                onValueChange(new)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            maxLines = 1,
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            label = { Text(text = label) },
            isError = errorMessage.isNullOrBlank().not(),
            supportingText = {
                if (errorMessage.isNullOrBlank().not()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(), text = "$errorMessage", color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorMessage.isNullOrBlank().not()) Icon(Icons.Filled.Clear, "error", tint = MaterialTheme.colorScheme.error)

            },
        )
    }
}

@Composable
fun StyledPasswordField(
    modifier: Modifier = Modifier,
    label: String,
    errorMessage: String?,
    keyboardType: KeyboardType = KeyboardType.Password,
    onValueChange: (String?) -> Unit
) {
    val value = remember { mutableStateOf("") }
    val shake = remember { Animatable(0f) }
    val hidePassword = remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage.isNullOrBlank().not()) {
            for (i in 0..6) {
                when (i % 2) {
                    0 -> shake.animateTo(5f, spring(stiffness = 100_00f))
                    else -> shake.animateTo(-5f, spring(stiffness = 100_00f))
                }
            }
            shake.animateTo(0f)
        }
    }

    TextField(
        modifier = if (errorMessage.isNullOrBlank().not()) modifier.offset {
            IntOffset(
                x = (shake.value * 10).roundToInt(), y = 0
            )
        }
        else modifier,
        value = value.value,
        onValueChange = { new: String ->
            value.value = new
            onValueChange(new)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = if (hidePassword.value) PasswordVisualTransformation()
        else VisualTransformation.None,
        maxLines = 1,
        textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
        label = { Text(text = label) },
        isError = errorMessage.isNullOrBlank().not(),
        supportingText = {
            if (errorMessage.isNullOrBlank().not()) {
                Text(
                    modifier = Modifier.fillMaxWidth(), text = "$errorMessage", color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                if (errorMessage.isNullOrBlank().not()) Icon(Icons.Filled.Clear, "error", tint = MaterialTheme.colorScheme.error)

                Icon(
                    if (hidePassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    if (hidePassword.value) "show password" else "hide password",
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.clickable {
                        hidePassword.value = !hidePassword.value
                    })
            }

        },
    )
}

@Composable
@Preview(showBackground = true)
fun StyledTextFieldPreview() {
    StyledTextField(label = "Name", errorMessage = null) {}
}

@Composable
@Preview(showBackground = true)
fun StyledTextFieldPreviewWitheError() {
    StyledTextField(label = "Name", errorMessage = "Error") {}
}
@Composable
@Preview(showBackground = true)
fun StyledTextFieldPassword() {
    StyledTextField(label = "Name", errorMessage = null, keyboardType = KeyboardType.Password) {}
}
@Composable
@Preview(showBackground = true)
fun StyledTextFieldPasswordWithError() {
    StyledTextField(label = "Name", errorMessage = "Error", keyboardType = KeyboardType.Password) {}
}