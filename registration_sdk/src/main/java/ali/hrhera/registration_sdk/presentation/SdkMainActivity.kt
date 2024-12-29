package ali.hrhera.registration_sdk.presentation

import ali.hrhera.registration_sdk.presentation.routs.AppRouts
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ali.hrhera.registration_sdk.presentation.ui.theme.RegistrationSDKTaskTheme
import androidx.activity.OnBackPressedCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SdkMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationSDKTaskTheme(darkTheme = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRouts( paddingValues = innerPadding)
                }
            }
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_CANCELED)
                finish()
            }

        })
    }
}



