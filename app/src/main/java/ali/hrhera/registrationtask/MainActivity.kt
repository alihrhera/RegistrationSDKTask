package ali.hrhera.registrationtask

import ali.hrhera.registration_sdk.presentation.SdkMainActivity
import ali.hrhera.registration_sdk.presentation.ui.component.VerticalSpace
import ali.hrhera.registrationtask.ui.theme.RegistrationSDKTaskTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationSDKTaskTheme(darkTheme = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainAppScreen {
                        resultLauncher.launch(Intent(this@MainActivity, SdkMainActivity::class.java))
                    }
                }
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.getStringExtra("")
            // Handle the received result
        }
    }
}

@Composable
fun MainAppScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Main Screen")
        VerticalSpace(height = 12)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Welcome to Registration App to start the registration process click on the button below and it will open " +
                    "the sdk to complete the registration process",
            fontSize = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        VerticalSpace(height = 24)
        ElevatedButton(onClick = onClick) {
            Text(text = "Start Registration Process")
        }

    }
}
