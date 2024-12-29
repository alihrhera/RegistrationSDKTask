package ali.hrhera.registration_sdk.presentation.routs

import ali.hrhera.registration_sdk.presentation.registration_form.RegisterScreen
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

@Composable
fun AppRouts(paddingValues: PaddingValues) {
    val navControl = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navControl) {
        NavHost(
            navController = navControl,
            startDestination = "start", modifier = Modifier.padding(paddingValues)
        ) {
            composable("start") {

            }
            composable("registerForm") {
                RegisterScreen(navControl)
            }
            composable("cameraStep/{userId}") {


            }
            composable("result/{userId}") {

            }
        }
    }
    val context = LocalContext.current as? Activity
    PressBackHandler(navControl, context)

}


@Composable
fun PressBackHandler(navControl: NavController, context: Activity?) {
    val backCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navControl.currentDestination?.route == "start"
                    ||
                    navControl.currentDestination?.route == "result"
                ) {
                    context?.setResult(Activity.RESULT_CANCELED)
                    context?.finish()
                } else navControl.popBackStack()
            }
        }
    val activity = (LocalContext.current as? ComponentActivity)
    if (activity is ComponentActivity) activity.onBackPressedDispatcher.addCallback(backCallback)
}