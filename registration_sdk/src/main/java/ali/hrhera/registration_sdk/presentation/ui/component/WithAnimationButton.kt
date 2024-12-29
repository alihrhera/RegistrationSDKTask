package ali.hrhera.registration_sdk.presentation.ui.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

sealed class ButtonState() {
    data class Loading(val text: String) : ButtonState()
    data class Success(val text: String) : ButtonState()
    data class Idle(val text: String) : ButtonState()
}

@Composable
fun ButtonWithAnimation(buttonState: ButtonState, onSuccess: () -> Unit, onClick: () -> Unit) {
    val shake = remember { Animatable(0f) }
    val trigger = remember { mutableLongStateOf(0L) }
    val colorAnimatable = remember { Animatable(Color.White) }

    LaunchedEffect("success") {
        if (trigger.longValue != 0L) {
            for (i in 0..4) {
                when (i % 2) {
                    0 -> shake.animateTo(5f, spring(stiffness = 100_0f))
                    else -> shake.animateTo(-5f, spring(stiffness = 100_0f))
                }
            }
            shake.animateTo(0f)
        }
    }
    LaunchedEffect(buttonState) {
        if (buttonState is ButtonState.Success) {
            colorAnimatable.animateTo(
                targetValue = Color.Green,
                animationSpec = tween(durationMillis = 1000)
            )
            onSuccess() // Perform action after the animation ends
        } else {
            colorAnimatable.snapTo(Color.White) // Reset color for other states
        }
    }

    ElevatedButton(
        modifier = if (buttonState is ButtonState.Success) Modifier
            .padding(32.dp)
            .fillMaxWidth()
            .offset {
                IntOffset(
                    x = 0, y = (shake.value * 10).roundToInt()
                )
            } else Modifier
            .padding(32.dp)
            .fillMaxWidth(),
        onClick = {
            onClick()
        }, colors = ButtonDefaults.elevatedButtonColors().copy(
            containerColor = colorAnimatable.value

        )
    ) {
        when (buttonState) {
            is ButtonState.Idle -> Text(text = buttonState.text)
            is ButtonState.Loading -> {
                Row(
                    modifier =
                    Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(text = buttonState.text)
                    HorizontalSpace(4)
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }

            is ButtonState.Success -> {
                trigger.longValue = System.currentTimeMillis()
                Text(text = buttonState.text, color = Color.White)
            }

        }

    }
}

