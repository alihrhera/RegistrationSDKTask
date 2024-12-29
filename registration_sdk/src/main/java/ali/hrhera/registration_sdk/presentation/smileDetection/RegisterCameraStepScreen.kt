package ali.hrhera.registration_sdk.presentation.smileDetection

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor


@Composable
fun RegisterCameraStepScreen(userId: Int, navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val viewmodel = hiltViewModel<RegisterCameraStepScreenViewModel>()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    val executor = ContextCompat.getMainExecutor(context)

    when (viewmodel.uiStat.value) {
        is RegisterCameraUiState.Success -> {
            navController.navigate("result/$userId")
            viewmodel.updateEvent(RegisterCameraUiState.Idel)
        }
        is RegisterCameraUiState.Idel -> {
            MainCamera(
                cameraProviderFuture,
                executor,
                viewmodel,
                lifecycleOwner,
                imageCapture,
                userId
            )
        }
    }


}

@Composable
private fun MainCamera(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    executor: Executor,
    viewmodel: RegisterCameraStepScreenViewModel,
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture,
    userId: Int
) {
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

                val imageAnalysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setImageQueueDepth(10).build().apply {
                        setAnalyzer(executor, viewmodel.faceDetectionUseCase())
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis, imageCapture
                )
            }, executor)
            previewView
        },
    )

    viewmodel.onTakePhoto = {
        // Take picture
        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val capturedBitmap: Bitmap = image.toBitmap()
                    viewmodel.saveImage(capturedBitmap, userId)
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("capturePhotoAndReplaceBackground", "onError: $", exception)
                }
            }
        )
    }



    Timer(viewModel = viewmodel)


}

@Composable
fun Timer(viewModel: RegisterCameraStepScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Please Keep smile and look to the camera", fontSize = 32.sp,
            style = TextStyle(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        if (viewModel.isReadyForCapture.value) {
            Text(
                text = "Stay tend will take photo in ${viewModel.time.intValue} seconds", fontSize = 32.sp,
                style = TextStyle(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center

            )
        }
    }
}


