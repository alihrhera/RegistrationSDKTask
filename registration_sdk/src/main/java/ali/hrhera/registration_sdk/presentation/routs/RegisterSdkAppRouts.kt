package ali.hrhera.registration_sdk.presentation.routs

import ali.hrhera.registration_sdk.presentation.features.registration_form.RegisterScreen
import ali.hrhera.registration_sdk.presentation.features.result.ResultScreen
import ali.hrhera.registration_sdk.presentation.features.smileDetection.RegisterCameraStepScreen
import ali.hrhera.registration_sdk.presentation.features.start.MainScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
const val SCREEN_TRANSITION_MILLIS = 500

@Composable
fun AppRouts(paddingValues: PaddingValues) {
    val navControl = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navControl) {
        NavHost(
            navController = navControl,
            startDestination = "start", modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(
                        SCREEN_TRANSITION_MILLIS
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(
                        SCREEN_TRANSITION_MILLIS
                    )
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(
                        SCREEN_TRANSITION_MILLIS
                    )
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(
                        SCREEN_TRANSITION_MILLIS
                    )
                )
            }

        ) {
            composable("start") {
                MainScreen(navControl)
            }
            composable("registerForm") {
                RegisterScreen(navControl)
            }
            composable("cameraStep/{userId}") {
                val userId = it.arguments?.getString("userId", "0")?.toInt() ?: 0
                RegisterCameraStepScreen(userId, navControl)

            }
            composable("result/{userId}") {
                val userId = it.arguments?.getString("userId", "0")?.toInt() ?: 0
                ResultScreen(userId)
            }
        }
    }

}


