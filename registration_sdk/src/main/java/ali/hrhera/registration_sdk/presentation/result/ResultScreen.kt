package ali.hrhera.registration_sdk.presentation.result

import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.presentation.ui.component.VerticalSpace
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ResultScreen(userId: Int) {
    val viewModel = hiltViewModel<ResultScreenViewModel>()
    val context = LocalContext.current
    val activity = context as? Activity

    Log.w("TAG", "ResultScreen: ${viewModel.uiStat.value}")
    when (val state = viewModel.uiStat.value) {

        is ResultScreenUiState.Error -> {
            state.message?.let {
                if (it.isNotBlank()) Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
            }
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }

        is ResultScreenUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ResultScreenUiState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ResultScreenContent(state.user)
                VerticalSpace(height = 16)
                ElevatedButton(onClick = {
                    val resultIntent = Intent().apply {
                        putExtra("user_id", state.user.id)
                    }
                    activity?.setResult(Activity.RESULT_OK, resultIntent)
                    activity?.finish()
                }) {
                    Text(text = "Finish")
                }
            }
        }

        is ResultScreenUiState.Idel -> {
            viewModel.getUserById(userId)
        }

    }
}

@Composable
fun ResultScreenContent(user: User) {
    Column(
        Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Result", fontSize = 32.sp, style = TextStyle(color = Color.Black))
        VerticalSpace(height = 32)
        Text(text = "Name: ${user.name}")
        VerticalSpace(height = 12)
        Text(text = "Phone: ${user.phone}")
        VerticalSpace(height = 12)

        Text(text = "Email: ${user.email}")
        VerticalSpace(height = 12)
        user.bitmaps.forEachIndexed { index, bitmap ->
            key(index + user.id) {
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Image", modifier = Modifier.size(120.dp))
            }
        }
    }
}
