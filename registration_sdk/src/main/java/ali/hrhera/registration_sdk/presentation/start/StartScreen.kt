package ali.hrhera.registration_sdk.presentation.start

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(controller: NavController) {
    val viewModel = hiltViewModel<StartViewModel>()
    val context = LocalContext.current as? Activity

    val cameraPermissionState = rememberPermissionState(permission = CAMERA) { isGranted ->
        if (!isGranted) {
            viewModel.updateEvent(StartScreenUiStat.Error)
        } else viewModel.updateEvent(StartScreenUiStat.PermissionGranted)
    }


    LaunchedEffect("AskForPermission") {
        viewModel.updateEvent(StartScreenUiStat.AskForPermission)
    }

    when (viewModel.startScreenUiStat.value) {
        StartScreenUiStat.AskForPermission -> {
            Box {
                cameraPermissionState.launchPermissionRequest()
            }
        }

        StartScreenUiStat.Idel -> {
        }

        StartScreenUiStat.PermissionGranted -> {
            controller.navigate("registerForm")
            viewModel.updateEvent(StartScreenUiStat.Idel)
        }

        StartScreenUiStat.Error -> {
            Toast.makeText(context, "Error Permission required ", Toast.LENGTH_LONG).show()
            ShouldShowRationale(context, cameraPermissionState)
        }

        StartScreenUiStat.None -> {}
    }

}

@Composable
fun PermissionRationale(onRequestPermission: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Handle dismissal */ },
        title = { Text(text = "Permission Required") },
        text = { Text(text = "This app requires camera access to take pictures.") },
        confirmButton = {
            TextButton(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShouldShowRationale(context: Activity?, permissionState: PermissionState) {
    if (permissionState.status.shouldShowRationale) {
        PermissionRationale(
            onRequestPermission = { permissionState.launchPermissionRequest() },
            onCancel = {
                context?.setResult(Activity.RESULT_CANCELED)
                context?.finish()
            })
    }
}
